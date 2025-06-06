package dev.slne.clan.core.spring

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import java.util.*

//@Configuration
class SpringCommonConfig {
    @Bean("clanObjectMapper")
    fun objectMapper(modules: ObjectProvider<Module>): JsonMapper = JsonMapper.builder()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addModules(modules.orderedStream().toList())
        .defaultTimeZone(TimeZone.getDefault())
        .build()

}
