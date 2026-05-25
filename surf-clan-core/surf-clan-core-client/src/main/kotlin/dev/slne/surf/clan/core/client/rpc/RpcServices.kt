package dev.slne.surf.clan.core.client.rpc

import dev.slne.surf.clan.core.client.rabbit.rabbitApi
import dev.slne.surf.clan.core.rpc.ClanInviteRpcService
import dev.slne.surf.clan.core.rpc.ClanMemberRpcService
import dev.slne.surf.clan.core.rpc.ClanPlayerRpcService
import dev.slne.surf.clan.core.rpc.ClanRpcService

val clanRpcService by lazy { rabbitApi.createRpcService<ClanRpcService>() }
val clanInviteRpcService by lazy { rabbitApi.createRpcService<ClanInviteRpcService>() }
val clanMemberRpcService by lazy { rabbitApi.createRpcService<ClanMemberRpcService>() }
val clanPlayerRpcService by lazy { rabbitApi.createRpcService<ClanPlayerRpcService>() }