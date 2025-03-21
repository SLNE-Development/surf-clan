package dev.slne.clan.core.utils

import it.unimi.dsi.fastutil.objects.ObjectArrayList

private val blacklist = mapOf(
    "nazi" to listOf(
        "NSD",
        "KRF",
        "HKN",
        "NPD",
        "KKK",
        "NAZI",
        "ZOG",
        "NSU",
        "SIEG",
        "HEIL",
        "NSM",
        "3RK",
        "BDM",
        "RSHA",
        "FUHR",
        "HITL",
        "HESS",
        "HIML",
        "GOEB",
        "GOER",
        "SIPO",
        "ORPO",
        "GEST",
        "SWAS",
        "REIC",
        "ARYN",
        "HTLR",
        "H!TL"
    ),
    "racism" to listOf(
        "NIG", "NGR", "SPC", "WET", "CHK", "GOK", "GYO", "GSL", "WOG", "JAP", "APE", "SPIC", "GYPS",
        "KIKE", "COON", "WOP", "MICK", "NIPS", "PAKI", "ABO", "LIME"
    ),
    "homophobia" to listOf(
        "FAG", "DYK", "HMO", "GAY", "H8G", "FAGG", "DYKE", "POOF", "GHEY", "HOMO",
        "SISS", "FRUT", "FAIR", "TWNK", "QWER", "LEZZ", "BUTCH", "BREA", "FART", "DQDE", "GAYY"
    ),
    "religious_intolerance" to listOf(
        "JEW", "MUS", "CHR", "HND", "BUD", "QUR", "TAO", "SIC", "CAL", "INF", "KFR", "SAT", "JUDE"
    ),
    "extremism" to listOf(
        "ISIS", "ALQ", "RPG", "WAR", "BLD", "KILL", "IRA", "PIRA", "ETA", "FARC", "AWD",
        "DAES", "TBAN", "AQIM", "ALF", "ELF", "NEON", "QDR", "SKIN", "TALI", "GAZA"
    ),
    "terror" to listOf(
        "911", "BMB", "JIH", "TBM", "TAL", "HAM", "HEZ", "BOKO", "ISL",
        "ASHB", "AQAP", "ISKP", "PKK", "LET", "LTTE", "WTC", "RAF", "PKKA",
    ),
    "politics" to listOf(
        "BNL", "NAZ", "ANT", "COM", "FAC", "SED", "RAF", "REP", "TRP", "KIM",
        "MAGA", "KGB", "CIA", "FBI", "QAN", "NWO"
    ),
    "insults" to listOf(
        "FCK", "WTF", "DIE", "BTC", "CNT", "DMB", "ASH", "LSR", "SHT", "DCK", "DICK",
        "SOB", "POS", "RET", "IDI", "LOS", "STF", "KRP", "SLT", "BSH", "PUS", "LRS", "STFU", "NOOB"
    ),
    "bullying" to listOf(
        "DUM", "IDI", "UGY", "FAT", "UGL", "LZR", "WRD", "PIG", "FUG", "SAD"
    ),
    "drugs" to listOf(
        "THC", "LSD", "KOK", "MDM", "GNG", "CRP", "DRG", "PNP", "MDMA", "DRUG", "CBD", "CRK",
        "MTH", "KET", "PEY", "FEN", "AMP", "WEED", "KOKS"
    ),
    "sexual" to listOf(
        "SEX", "FUC", "XXX", "BBC", "DCK", "TTS", "VGN", "CUM", "FUCK", "ASS", "ANL", "MILF",
        "ANAL", "COC", "TIT", "DIL", "CUCK", "BICH", "PORN", "BJOB", "GANG", "COCK"
    ),
    "violence" to listOf(
        "GUN", "BOM", "KLL", "RPE", "ABU", "HRT", "SHO", "STB", "HIT", "KIK", "BASH", "MAME"
    ),
    "selfharm" to listOf(
        "CUT", "SUI", "KYS", "SLF", "HURT", "SLIT", "BURN", "HANG", "JUMP"
    ),
    "offensive" to listOf(
        "DUM",
        "DUMM",
        "IDI",
        "STF",
        "NOO",
        "SUQ",
        "FUQ",
        "GFY",
        "SMD",
        "FUX",
        "LOL",
        "SUK",
        "YDI",
        "KACK",
        "SHIT",
        "FURZ",
        "KEK"
    ),
    "cybercrime" to listOf(
        "DDOS", "PISH", "SCAM", "HAX", "BOT", "TOR", "DOX", "RAT", "HACK"
    ),
    "gore" to listOf(
        "BLOD", "CORE", "GORE", "GUTS", "AMPT", "GASH", "SLSH", "SKIN"
    ),
    "pre-blocked" to listOf(
        "GHG",
        "XYZ",
        "AAA",
        "PISS",
        "LGBT",
        "TEAM",
        "CAST",
        "PEDO",
        "QWE",
        "MAMA",
        "PAPA",
        "MUM",
        "DAD",
        "MUMY",
        "DADY",
        "TOD",
        "GRWM",
        "BONG",
        "RUSS",
        "RUS",
        "MUSK",
        "ROMA",
        "TEST",
        "MÜLL",
        "AFK",
        "MOD",
        "DEV",
        "HELP",
        "SCAM"
    ),
    "transphobia" to listOf(
        "TRN", "TGM", "H8T"
    ),
    "misogyny" to listOf(
        "HOE", "BCH", "COW", "SLT"
    ),
    "parties" to listOf(
        "CDU", "SPD", "FDP", "CSU", "GRN", "LNK", "GOP", "DEM", "LAB", "CON", "UKP", "BREX",
        "M5S", "LEG", "SNP", "BSW", "AFD", "3WEG"
    ),
    "ableism" to listOf(
        "TARD", "GIMP", "SPAZ", "SPST", "KRPL"
    ),
    "child_abuse" to listOf(
        "CHAB", "CHLD", "MINR", "PHIL"
    ),
    "bestiality" to listOf(
        "ZOOP", "BEAS", "ANML"
    ),
    "incest" to listOf(
        "INCE"
    ),
    "xenophobia" to listOf(
        "FORE", "XENO"
    ),
    "mafia" to listOf(
        "MOB", "MFA", "COSA"
    ),
    "cartels" to listOf(
        "CDG", "ZETA", "SICR"
    )
)

private val performantBlacklist =
    ObjectArrayList(blacklist.flatMap { it.value }.map { it.lowercase() })

fun isInvalidClanTag(clanTag: String) = performantBlacklist.contains(clanTag.lowercase())


private val translatedLettersToGlyph = mapOf(
    "a" to "Ꝗ",
    "b" to "ꝗ",
    "c" to "Ꝙ",
    "d" to "ꝙ",
    "e" to "Ꝛ",
    "f" to "ꝛ",
    "g" to "Ꝝ",
    "h" to "ꝝ",
    "i" to "Ꝟ",
    "j" to "ꝟ",
    "k" to "Ꝡ",
    "l" to "ꝡ",
    "m" to "Ꝣ",
    "n" to "ꝣ",
    "o" to "Ꝥ",
    "p" to "ꝥ",
    "q" to "Ꝧ",
    "r" to "ꝧ",
    "s" to "Ꝩ",
    "t" to "ꝩ",
    "u" to "Ꝫ",
    "v" to "ꝫ",
    "w" to "Ꝭ",
    "x" to "ꝭ",
    "y" to "Ꝯ",
    "z" to "ꝯ",
    "0" to "ꝰ",
    "1" to "ꝱ",
    "2" to "ꝲ",
    "3" to "ꝳ",
    "4" to "ꝴ",
    "5" to "ꝵ",
    "6" to "ꝶ",
    "7" to "ꝷ",
    "8" to "ꝸ",
    "9" to "Ꝺ",
)

private val negSpaceChar = "ꑛ"
private val spacerOneChar = "ꝑ"
private val spacerTwoChar = "Ꝓ"

private fun translateToGlyphs(input: String) =
    input.map { translatedLettersToGlyph[it.toString().lowercase()] ?: it }.toList()

fun translateClanTag(input: String): String {
    val translated = translateToGlyphs(input)

    // insert negative space characters between each character
    // insert two char spacer up front and one char spacer at the end
    return "$spacerTwoChar$negSpaceChar${translated.joinToString("$negSpaceChar")}$negSpaceChar$spacerOneChar"
}