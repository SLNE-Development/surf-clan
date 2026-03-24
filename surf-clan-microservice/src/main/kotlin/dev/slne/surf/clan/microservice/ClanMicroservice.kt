package dev.slne.surf.clan.microservice

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.ClanCoreSerializerModule
import dev.slne.surf.clan.microservice.db.table.ClanInvitesTable
import dev.slne.surf.clan.microservice.db.table.ClanMembersTable
import dev.slne.surf.clan.microservice.db.table.ClanPlayerTable
import dev.slne.surf.clan.microservice.db.table.ClansTable
import dev.slne.surf.clan.microservice.handler.clan.ClanCreateHandler
import dev.slne.surf.clan.microservice.handler.clan.ClanDeleteHandler
import dev.slne.surf.clan.microservice.handler.clan.FindAllClansWithoutMembersSortByMemberCountHandler
import dev.slne.surf.clan.microservice.handler.clan.FindClanByClanIDHandler
import dev.slne.surf.clan.microservice.handler.clan.FindClanByMemberHandler
import dev.slne.surf.clan.microservice.handler.clan.FindClanByTagHandler
import dev.slne.surf.clan.microservice.handler.clan.FindClanByUuidHandler
import dev.slne.surf.clan.microservice.handler.clan.FindClanTagsByPrefixLimitedHandler
import dev.slne.surf.clan.microservice.handler.clan.UpdateClanDescriptionHandler
import dev.slne.surf.clan.microservice.handler.clan.UpdateClanDiscordInviteHandler
import dev.slne.surf.clan.microservice.handler.clan.UpdateClanTagColorHandler
import dev.slne.surf.clan.microservice.handler.invite.AcceptClanInviteHandler
import dev.slne.surf.clan.microservice.handler.invite.CreateClanInviteHandler
import dev.slne.surf.database.DatabaseApi
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.microservice.api.microservice.Microservice
import dev.slne.surf.rabbitmq.api.ServerRabbitMQApi
import kotlin.io.path.Path

@AutoService(Microservice::class)
class ClanMicroservice : Microservice() {
    private val configPath = Path("config")
    val databaseApi = DatabaseApi.create(configPath)
    val rabbitApi = ServerRabbitMQApi.create("surf-clan", configPath, ClanCoreSerializerModule.module)

    override suspend fun onBootstrap(args: List<String>) {
        createTables()

        // Clan
        rabbitApi.registerRequestHandler(ClanCreateHandler)
        rabbitApi.registerRequestHandler(ClanDeleteHandler)
        rabbitApi.registerRequestHandler(FindAllClansWithoutMembersSortByMemberCountHandler)
        rabbitApi.registerRequestHandler(FindClanByClanIDHandler)
        rabbitApi.registerRequestHandler(FindClanByMemberHandler)
        rabbitApi.registerRequestHandler(FindClanByTagHandler)
        rabbitApi.registerRequestHandler(FindClanByUuidHandler)
        rabbitApi.registerRequestHandler(FindClanTagsByPrefixLimitedHandler)
        rabbitApi.registerRequestHandler(UpdateClanDescriptionHandler)
        rabbitApi.registerRequestHandler(UpdateClanDiscordInviteHandler)
        rabbitApi.registerRequestHandler(UpdateClanTagColorHandler)

        // Invite
        rabbitApi.registerRequestHandler(AcceptClanInviteHandler)
        rabbitApi.registerRequestHandler(CreateClanInviteHandler)

        rabbitApi.freezeAndConnect()
    }

    private suspend fun createTables() = suspendTransaction {
        SchemaUtils.create(
            ClansTable,
            ClanMembersTable,
            ClanPlayerTable,
            ClanInvitesTable
        )
    }

    override suspend fun onDisable() {
        rabbitApi.disconnect()
        databaseApi.shutdown()
    }
}