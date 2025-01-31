package dev.slne.clan.core.utils

private val blacklist = mapOf(
    "nazi" to listOf("NSD", "SS", "HH", "88", "18", "14", "KRF", "HKN", "NPD", "KKK", "NAZI"),
    "insults" to listOf(
        "FCK",
        "WTF",
        "KYS",
        "DIE",
        "NGR",
        "BTC",
        "CNT",
        "DMB",
        "ASH",
        "LSR",
        "SHT",
        "DCK",
        "DICK"
    ),
    "extremism" to listOf("ISIS", "ALQ", "BOM", "RPG", "WAR", "KLL", "GUN", "BLD", "KILL"),
    "drugs" to listOf("THC", "LSD", "KOK", "MDM", "GNG", "CRP", "DRG", "PNP", "MDMA", "DRUG"),
    "sexual" to listOf("SEX", "FUC", "XXX", "BBC", "DCK", "TTS", "VGN", "CUM", "FUCK"),
    "politics" to listOf("BNL", "NAZ", "JIH", "ANT", "COM", "FAC"),
    "offensive" to listOf("DUM", "IDI", "STF", "BS", "NOO", "SUQ", "FUQ"),
    "pre-blocked" to listOf("CC", "GHG")
)

fun isInvalidClanTag(clanTag: String) = blacklist
    .flatMap { it.value }
    .map { it.lowercase() }
    .contains(clanTag.lowercase())