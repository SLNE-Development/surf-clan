package dev.slne.clan.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

fun LocalDateTime.formatted() = format(DATE_TIME_FORMATTER)