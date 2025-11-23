package dev.slne.surf.clan.core.common.utils

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun ZonedDateTime.format(): String = format(formatter)
fun ZonedDateTime.formatComponent() = buildText {
    variableValue(format())
}