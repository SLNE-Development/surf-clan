package dev.slne.clan.core.invite

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
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
@Table(name = "clan_invites")
@EntityListeners(AuditingEntityListener::class)
data class CoreClanInvite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "invited", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val invited: UUID,

    @Column(name = "invited_by", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val invitedByUuid: UUID,

    @ManyToOne(targetEntity = CoreClan::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    val clan: Clan,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override val createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    override val updatedAt: LocalDateTime? = LocalDateTime.now()
) : ClanInvite {

    override val invitedBy: ClanMember?
        get() = clan.members.find { it.uuid == invitedByUuid }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CoreClanInvite

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    override fun toString(): String {
        return "CoreClanInvite(id=$id, invited=$invited, invitedByUuid=$invitedByUuid, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}