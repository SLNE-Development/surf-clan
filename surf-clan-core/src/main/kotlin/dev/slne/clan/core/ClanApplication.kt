package dev.slne.clan.core

import dev.slne.clan.core.spring.JoinClassLoader
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.nio.file.Path

lateinit var dataDirectory: Path

lateinit var _context: ConfigurableApplicationContext

val context: ConfigurableApplicationContext
    get() {
        if (!::_context.isInitialized) {
            error("Application not running")
        }

        return _context
    }

fun runApplication(classLoader: ClassLoader, vararg parentClassLoader: ClassLoader) {
    if (::_context.isInitialized) {
        error("Application already running")
    }

    val contextClassLoader = Thread.currentThread().contextClassLoader
    val joinClassLoader = JoinClassLoader(classLoader, parentClassLoader)

    Thread.currentThread().contextClassLoader = joinClassLoader

    _context = SpringApplicationBuilder(ClanApplication::class.java)
        .bannerMode(Banner.Mode.OFF)
        .profiles("production")
        .run()

    Thread.currentThread().contextClassLoader = contextClassLoader
}

inline fun <reified B : Any> getBean(): B = _context.getBean(B::class.java)

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class],
    scanBasePackages = ["dev.slne.clan"]
)
@EnableCaching
@EnableJpaRepositories(basePackages = ["dev.slne.clan"])
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackages = ["dev.slne.clan"])
class ClanApplication