@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.ClanMemberManagementDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.results.createClanMemberKickNoticeDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import net.kyori.adventure.text.Component

fun ClanMemberManagementDialog.createKickMemberButton(
    selfClanPlayer: ClanPlayer,
    clanMember: ClanMember,
    clanMemberDisplayName: Component,
    clanPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text("Entfernen") }
    tooltip { info("Klicke, um das Mitglied aus dem Clan zu entfernen.") }
    width(200)

    action {
        playerCallback { player ->

            plugin.launch {
                val result = clan.canRemove(selfClanPlayer, clanPlayer)

                player.showDialog(
                    createClanMemberKickNoticeDialog(
                        result,
                        selfClanPlayer,
                        clanMember,
                        clanMemberDisplayName,
                        clanPlayer,
                        clan
                    )
                )
            }
        }
    }
}