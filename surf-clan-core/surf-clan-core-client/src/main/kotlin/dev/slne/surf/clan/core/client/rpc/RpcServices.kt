package dev.slne.surf.clan.core.client.rpc

import dev.slne.surf.clan.core.client.rabbit.rabbitApi
import dev.slne.surf.clan.core.rpc.ClanRpcService

val clanRpcService by lazy { rabbitApi.createRpcService<ClanRpcService>() }