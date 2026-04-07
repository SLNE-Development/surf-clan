package dev.slne.surf.clan.core

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.clan.core.member.ClanMemberImpl
import kotlinx.serialization.modules.SerializersModule

object ClanCoreSerializerModule {
    val module = SerializersModule {
        polymorphic(ClanInvite::class, ClanInviteImpl::class, ClanInviteImpl.serializer())
        polymorphic(ClanMember::class, ClanMemberImpl::class, ClanMemberImpl.serializer())
        polymorphic(Clan::class, ClanImpl::class, ClanImpl.serializer())
    }
}