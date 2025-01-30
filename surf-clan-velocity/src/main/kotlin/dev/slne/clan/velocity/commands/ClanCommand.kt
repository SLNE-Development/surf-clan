package dev.slne.clan.velocity.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.service.NameCacheService
import dev.slne.clan.velocity.commands.subcommands.ClanCreateCommand
import dev.slne.clan.velocity.commands.subcommands.ClanDisbandCommand
import dev.slne.clan.velocity.commands.subcommands.ClanInfoCommand
import dev.slne.clan.velocity.commands.subcommands.ClanLeaveCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanKickMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanMembersCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanInviteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanDemoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanPromoteMemberCommand

class ClanCommand(
    clanService: ClanService,
    nameCacheService: NameCacheService
) : CommandAPICommand("clan") {
    init {
        withSubcommand(ClanCreateCommand(clanService, nameCacheService))
        withSubcommand(ClanDisbandCommand(clanService, nameCacheService))
        withSubcommand(ClanLeaveCommand(clanService, nameCacheService))
        withSubcommand(ClanInfoCommand(clanService, nameCacheService))

        withSubcommand(ClanInviteMemberCommand(clanService, nameCacheService))
        withSubcommand(ClanPromoteMemberCommand(clanService, nameCacheService))
        withSubcommand(ClanDemoteMemberCommand(clanService, nameCacheService))
        withSubcommand(ClanKickMemberCommand(clanService, nameCacheService))
        withSubcommand(ClanMembersCommand(clanService, nameCacheService))
    }
}