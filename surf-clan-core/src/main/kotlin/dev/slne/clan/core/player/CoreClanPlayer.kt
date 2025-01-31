package dev.slne.clan.core.player

import dev.slne.clan.api.player.ClanPlayer
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.proxy.HibernateProxy
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "clan_players")
data class CoreClanPlayer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username", length = 16, nullable = false)
    override var username: String,

    @Column(name = "uuid", length = 36, unique = true, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val uuid: UUID,

    @Column(name = "accepts_clan_invites", nullable = false)
    override var acceptsClanInvites: Boolean = true
) : ClanPlayer {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CoreClanPlayer

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    override fun toString(): String {
        return "CoreClanPlayer(id=$id, username='$username', uuid=$uuid, acceptsClanInvites=$acceptsClanInvites)"
    }

}