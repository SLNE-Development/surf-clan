package dev.slne.clan.core.member

import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.CoreClan
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
@Table(name = "clan_members")
@EntityListeners(AuditingEntityListener::class)
data class CoreClanMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "uuid", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val uuid: UUID,

    @Column(name = "added_by", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private val _addedBy: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    override var role: ClanMemberRole,

    @ManyToOne(targetEntity = CoreClan::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    val clan: Clan,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override var createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override var updatedAt: LocalDateTime? = LocalDateTime.now()
) : ClanMember {

    override val addedBy: ClanMember?
        get() = clan.members.find { it.uuid == _addedBy }

    override fun hasPermission(clanPermission: ClanPermission) = role.hasPermission(clanPermission)

    override fun toString(): String {
        return "CoreClanMember(id=$id, uuid=$uuid, _addedBy=$_addedBy, role=$role, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CoreClanMember

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

}