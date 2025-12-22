package dev.slne.surf.clan.fallback.repository

import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.clan.fallback.table.ClanPlayerTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.upsert
import java.util.*

val clanPlayerRepository = ClanPlayerRepository()

class ClanPlayerRepository {
    suspend fun findFirstByUuid(uuid: UUID): ClanPlayer? = newSuspendedTransaction(Dispatchers.IO) {
        ClanPlayerTable.selectAll().where(ClanPlayerTable.uuid eq uuid).firstOrNull()?.let { row ->
            ClanPlayer(
                uuid = uuid,
                username = row[ClanPlayerTable.username],
                acceptsClanInvites = row[ClanPlayerTable.acceptsClanInvites]
            )
        }
    }

    suspend fun findFirstByUsername(username: String): ClanPlayer? =
        newSuspendedTransaction(Dispatchers.IO) {
            ClanPlayerTable.selectAll().where(ClanPlayerTable.username eq username).firstOrNull()
                ?.let { row ->
                    ClanPlayer(
                        uuid = row[ClanPlayerTable.uuid],
                        username = row[ClanPlayerTable.username],
                        acceptsClanInvites = row[ClanPlayerTable.acceptsClanInvites]
                    )
                }
        }

    suspend fun save(clanPlayer: ClanPlayer): ClanPlayer {
        return newSuspendedTransaction(Dispatchers.IO) {
            ClanPlayerTable.upsert {
                it[uuid] = clanPlayer.uuid
                it[username] = clanPlayer.username
                it[acceptsClanInvites] = clanPlayer.acceptsClanInvites
            }
            clanPlayer
        }
    }
}