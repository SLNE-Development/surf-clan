package dev.slne.clan.velocity.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.core.redis.RedisService
import dev.slne.clan.velocity.commands.arguments.OfflinePlayerArgument
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.clan.velocity.redis.event.ClanInviteRedisEvent
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.surfapi.core.api.command.args.awaiting
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend

fun CommandAPICommand.clanInviteCommand() = subcommand("invite") {
    withPermission(ClanPermissions.CLAN_INVITE_COMMAND)

    arguments(OfflinePlayerArgument("invitee"))

    playerExecutorSuspend { player, args ->
        val invitee = args.awaiting<SurfPlayer>("invitee")
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.INVITE)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, Spieler in den Clan einzuladen.")
        }

        when (clan.invite(invitee.uuid, player.uniqueId)) {
            ClanInviteResult.AlreadyInClan -> throw CommandAPI.failWithString("Der Spieler ist bereits in einem Clan.")
            ClanInviteResult.AlreadyInvited -> throw CommandAPI.failWithString("Der Spieler wurde bereits eingeladen.")
            ClanInviteResult.InvitationsDisabled -> throw CommandAPI.failWithString("Der Spieler nimmt keine Einladungen an.")
            is ClanInviteResult.Success -> {
                player.sendText {
                    appendPrefix()
                    success("Du hast ")
                    variableValue(invitee.lastKnownName ?: invitee.uuid.toString())
                    success(" in den Clan ")
                    append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
                    success(" eingeladen.")
                }

                RedisService.publish(ClanInviteRedisEvent(player.username, invitee.uuid, clan.name)).await()
            }
        }
    }
}