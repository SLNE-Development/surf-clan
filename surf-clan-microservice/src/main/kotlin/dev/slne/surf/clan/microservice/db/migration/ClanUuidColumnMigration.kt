package dev.slne.surf.clan.microservice.db.migration

import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.vendors.currentDialect
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.TransactionManager

private data class UuidColumn(
    val table: String,
    val column: String,
)

private val uuidColumns = listOf(
    UuidColumn("clan_clans", "uuid"),
    UuidColumn("clan_clans", "created_by"),
    UuidColumn("clan_members", "uuid"),
    UuidColumn("clan_members", "added_by"),
    UuidColumn("clan_players", "uuid"),
    UuidColumn("clan_invites", "invited"),
    UuidColumn("clan_invites", "invited_by"),
)

/**
 * Converts UUID columns created by the legacy MariaDB schema as text into native PostgreSQL UUID
 * columns. Exposed's SchemaUtils creates missing tables and columns, but it does not change the type
 * of an existing column.
 *
 * The migration is safe to execute on every startup. Columns that are already UUID are skipped. If
 * a legacy column contains a malformed UUID, PostgreSQL aborts startup instead of silently changing
 * or discarding data.
 */
suspend fun migrateLegacyClanUuidColumns() {
    if (currentDialect !is PostgreSQLDialect) return

    val transaction = TransactionManager.current()
    for ((table, column) in uuidColumns) {
        transaction.exec(
            """
            DO ${'$'}migration${'$'}
            DECLARE
                legacy_type text;
            BEGIN
                SELECT data_type
                INTO legacy_type
                FROM information_schema.columns
                WHERE table_schema = current_schema()
                  AND table_name = '$table'
                  AND column_name = '$column';

                IF legacy_type IN ('text', 'character varying', 'character') THEN
                    EXECUTE format(
                        'ALTER TABLE %I.%I ALTER COLUMN %I TYPE uuid USING NULLIF(BTRIM(%I::text), '''')::uuid',
                        current_schema(),
                        '$table',
                        '$column',
                        '$column'
                    );
                END IF;
            END;
            ${'$'}migration${'$'};
            """.trimIndent()
        )
    }
}
