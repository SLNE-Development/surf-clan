package dev.slne.clan.core.name

import dev.slne.clan.api.name.NameCache
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.proxy.HibernateProxy
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "clan_name_cache")
data class CoreNameCache(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username", length = 16)
    override var username: String,

    @Column(name = "uuid", length = 36, unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    override val uuid: UUID,
) : NameCache {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CoreNameCache

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    override fun toString(): String {
        return "CoreNameCache(id=$id, username='$username', uuid=$uuid)"
    }

}