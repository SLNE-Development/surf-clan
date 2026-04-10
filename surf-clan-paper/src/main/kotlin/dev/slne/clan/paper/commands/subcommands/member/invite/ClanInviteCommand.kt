package dev.slne.clan.paper.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.arguments.OfflinePlayerArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.common.util.sendText
import net.kyori.adventure.text.event.ClickEvent

fun CommandAPICommand.clanInviteCommand() = subcommand("invite") {
    withPermission(ClanPermissions.CLAN_INVITE_COMMAND)

    arguments(OfflinePlayerArgument("invitee"))

    playerExecutorSuspend { player, args ->
        val invitee = args.awaiting<SurfPlayer>("invitee")
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.INVITE)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, Spieler in den Clan einzuladen.")
        }

        when (clan.invite(invitee.uuid, player.uniqueId)) {
            ClanInviteResult.AlreadyInClan -> throw CommandAPI.failWithString("Der Spieler ist bereits in einem Clan.")
            ClanInviteResult.AlreadyInvited -> throw CommandAPI.failWithString("Der Spieler wurde bereits eingeladen.")
            ClanInviteResult.InvitationsDisabled -> throw CommandAPI.failWithString("Der Spieler nimmt keine Einladungen an.")
            is ClanInviteResult.Success -> {
                player.sendText {
                    appendSuccessPrefix()
                    success("Du hast ")
                    variableValue(invitee.lastKnownName ?: invitee.uuid.toString())
                    success(" in den Clan ")
                    append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
                    success(" eingeladen.")
                }

                SurfCoreApi.getPlayer(invitee.uuid)?.sendText {
                    appendInfoPrefix()
                    info("Du wurdest von ")
                    variableValue(player.name)
                    info(" in den Clan ")
                    variableValue(clan.name)
                    info(" eingeladen. ")

                    append {
                        success("[Annehmen]")
                        hoverEvent(buildText {
                            info("Klicke hier, um die Einladung anzunehmen.")
                        })
                        clickEvent(ClickEvent.runCommand("/clan accept ${clan.name}"))
                    }

                    appendSpace()

                    append {
                        error("[Ablehnen]")
                        hoverEvent(buildText {
                            info("Klicke hier, um die Einladung abzulehnen.")
                        })
                        clickEvent(ClickEvent.runCommand("/clan deny ${clan.name}"))
                    }
                }
            }
        }
    }
}