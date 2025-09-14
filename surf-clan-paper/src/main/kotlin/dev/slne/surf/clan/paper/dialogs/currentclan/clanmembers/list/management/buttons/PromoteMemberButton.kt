@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.ClanMemberManagementDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.results.createClanMemberSetRoleNoticeDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import net.kyori.adventure.text.Component

fun ClanMemberManagementDialog.createPromoteMemberButton(
    selfClanPlayer: ClanPlayer,
    clanMember: ClanMember,
    clanMemberDisplayName: Component,
    clanPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text("Befördern") }
    tooltip { info("Klicke, um das Mitglied zu befördern.") }
    width(200)

    action {
        playerCallback { player ->
            val currentRole = clanMember.role
            val newRole = currentRole.nextRole()

            plugin.launch {
                val result = clanMember.setRole(newRole, selfClanPlayer)

                player.showDialog(
                    createClanMemberSetRoleNoticeDialog(
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