package dev.slne.clan.core

import dev.slne.data.api.DataApi
import dev.slne.data.api.spring.SurfSpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.nio.file.Path

lateinit var dataDirectory: Path
private lateinit var _context: ConfigurableApplicationContext

val context: ConfigurableApplicationContext
    get() {
        if (!::_context.isInitialized) {
            error("Application not running")
        }

        return _context
    }

inline fun <reified B : Any> getBean(): B = context.getBean(B::class.java)


@SurfSpringApplication(
    scanBasePackages = ["dev.slne.clan"],
    scanFeignBasePackages = ["dev.slne.clan"]
)
@EnableAutoConfiguration(exclude = [RedisAutoConfiguration::class, RedisRepositoriesAutoConfiguration::class, SpringDataWebAutoConfiguration::class])
@EnableCaching
@EnableJpaRepositories(basePackages = ["dev.slne.clan"])
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackages = ["dev.slne.clan"])
class ClanApplication {
    companion object {
        fun run(classLoader: ClassLoader) = DataApi.run(ClanApplication::class.java, classLoader).also { _context = it }
    }

}