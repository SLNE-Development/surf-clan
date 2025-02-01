package dev.slne.clan.core

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.member.CoreClanMember
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSets
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.TimeZoneStorage
import org.hibernate.annotations.TimeZoneStorageType
import org.hibernate.proxy.HibernateProxy
import org.hibernate.type.SqlTypes
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "clans")
@EntityListeners(AuditingEntityListener::class)
data class CoreClan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "uuid", nullable = false, length = 36, unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val uuid: UUID,

    @Column(name = "name", nullable = false, unique = true)
    override val name: String,

    @Column(name = "tag", nullable = false, unique = true)
    override val tag: String,

    @Column(name = "description", nullable = true, length = 255)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    override val description: String? = null,

    @Column(name = "created_by", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val createdBy: UUID,

    @Column(name = "discord_invite", nullable = true)
    override val discordInvite: String? = null,

    @OneToMany(
        mappedBy = "clan",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        targetEntity = CoreClanMember::class,
        fetch = FetchType.EAGER
    )
    private val _members: MutableSet<ClanMember> = mutableSetOf(),

    @OneToMany(
        mappedBy = "clan",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        targetEntity = CoreClanInvite::class,
        fetch = FetchType.EAGER
    )
    private var _invites: MutableSet<ClanInvite> = mutableSetOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override var createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override var updatedAt: LocalDateTime? = LocalDateTime.now()
) : Clan {

    @get:Transient
    override val members: ObjectSet<ClanMember>
        get() = ObjectSets.unmodifiable(ObjectOpenHashSet(_members))

    @get:Transient
    override val invites: ObjectSet<ClanInvite>
        get() = ObjectSets.unmodifiable(ObjectOpenHashSet(_invites))

    override fun invite(uuid: UUID, invitedBy: UUID) = _invites.add(
        CoreClanInvite(
            clan = this,
            invited = uuid,
            invitedByUuid = invitedBy
        )
    )

    override fun uninvite(uuid: UUID) = _invites.removeIf { it.invited == uuid }

    override fun isMember(uuid: UUID) = members.any { it.uuid == uuid }
    override fun addMember(member: ClanMember) = _members.add(member)
    override fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID) =
        addMember(
            CoreClanMember(
                uuid = uuid,
                _addedBy = addedBy,
                role = role,
                clan = this,
            )
        )

    override fun removeMember(member: ClanMember) = _members.remove(member)

    override fun hasPermission(clanMember: ClanMember, permission: ClanPermission) =
        clanMember.role.hasPermission(permission)

    override fun getMember(clanPlayer: ClanPlayer): ClanMember? = members.find { it.uuid == clanPlayer.uuid }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CoreClan

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    override fun toString(): String {
        return "CoreClan(id=$id, name='$name', tag='$tag', discordInvite=$discordInvite, _members=$_members, members=$members, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}