package dev.slne.surf.clan.core.clan

import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object ClanTagRules {
    private val prohibitedTags = ProhibitedCategory.entries
        .flatMap { it.tags }
        .mapTo(mutableObjectSetOf()) { it.uppercase() }

    fun isValid(tag: String): Boolean =
        tag.uppercase() !in prohibitedTags

    enum class ProhibitedCategory(val tags: Set<String>) {
        NAZISM(
            objectSetOf(
                "NSD", "KRF", "HKN", "NPD", "KKK", "NAZI", "ZOG", "NSU", "SIEG", "HEIL",
                "NSM", "3RK", "BDM", "RSHA", "FUHR", "HITL", "HESS", "HIML", "GOEB", "GOER",
                "SIPO", "ORPO", "GEST", "SWAS", "REIC", "ARYN", "HTLR", "H!TL"
            )
        ),
        RACISM(
            objectSetOf(
                "NIG", "NGR", "SPC", "WET", "CHK", "GOK", "GYO", "GSL", "WOG", "JAP", "APE",
                "SPIC", "GYPS", "KIKE", "COON", "WOP", "MICK", "NIPS", "PAKI", "ABO", "LIME"
            )
        ),
        HOMOPHOBIA(
            objectSetOf(
                "FAG", "DYK", "HMO", "GAY", "H8G", "FAGG", "DYKE", "POOF", "GHEY", "HOMO", "SISS",
                "FRUT", "FAIR", "TWNK", "QWER", "LEZZ", "BUTCH", "BREA", "FART", "DQDE", "GAYY"
            )
        ),
        TRANSPHOBIA(objectSetOf("TRN", "TGM", "H8T")),
        MISOGYNY(objectSetOf("HOE", "BCH", "COW", "SLT")),
        RELIGIOUS_INTOLERANCE(
            objectSetOf(
                "JEW", "MUS", "CHR", "HND", "BUD", "QUR", "TAO",
                "SIC", "CAL", "INF", "KFR", "SAT", "JUDE"
            )
        ),
        EXTREMISM(
            objectSetOf(
                "ISIS", "ALQ", "RPG", "WAR", "BLD", "KILL", "IRA", "PIRA", "ETA", "FARC", "AWD",
                "DAES", "TBAN", "AQIM", "ALF", "ELF", "NEON", "QDR", "SKIN", "TALI", "GAZA"
            )
        ),
        TERROR(
            objectSetOf(
                "911", "BMB", "JIH", "TBM", "TAL", "HAM", "HEZ", "BOKO", "ISL",
                "ASHB", "AQAP", "ISKP", "PKK", "LET", "LTTE", "WTC", "RAF", "PKKA",
            )
        ),
        POLITICS(
            objectSetOf(
                "BNL", "NAZ", "ANT", "COM", "FAC", "SED", "RAF", "REP",
                "TRP", "KIM", "MAGA", "KGB", "CIA", "FBI", "QAN", "NWO"
            )
        ),
        INSULTS(
            objectSetOf(
                "FCK", "WTF", "DIE", "BTC", "CNT", "DMB", "ASH", "LSR", "SHT",
                "DCK", "DICK", "SOB", "POS", "RET", "IDI", "LOS", "STF", "KRP",
                "SLT", "BSH", "PUS", "LRS", "STFU", "NOOB"
            )
        ),
        BULLYING(objectSetOf("DUM", "IDI", "UGY", "FAT", "UGL", "LZR", "WRD", "PIG", "FUG", "SAD")),
        DRUGS(
            objectSetOf(
                "THC", "LSD", "KOK", "MDM", "GNG", "CRP", "DRG", "PNP", "MDMA", "DRUG",
                "CBD", "CRK", "MTH", "KET", "PEY", "FEN", "AMP", "WEED", "KOKS"
            )
        ),
        SEXUAL(
            objectSetOf(
                "SEX",
                "FUC",
                "XXX",
                "BBC",
                "DCK",
                "TTS",
                "VGN",
                "CUM",
                "FUCK",
                "ASS",
                "ANL",
                "MILF",
                "ANAL",
                "COC",
                "TIT",
                "DIL",
                "CUCK",
                "BICH",
                "PORN",
                "BJOB",
                "GANG",
                "COCK"
            )
        ),
        VIOLENCE(
            objectSetOf(
                "GUN",
                "BOM",
                "KLL",
                "RPE",
                "ABU",
                "HRT",
                "SHO",
                "STB",
                "HIT",
                "KIK",
                "BASH",
                "MAME"
            )
        ),
        SELF_HARM(objectSetOf("CUT", "SUI", "KYS", "SLF", "HURT", "SLIT", "BURN", "HANG", "JUMP")),
        OFFENSIVE(
            objectSetOf(
                "DUM", "DUMM", "IDI", "STF", "NOO", "SUQ", "FUQ", "GFY", "SMD",
                "FUX", "LOL", "SUK", "YDI", "KACK", "SHIT", "FURZ", "KEK"
            )
        ),
        CYBERCRIME(objectSetOf("DDOS", "PISH", "SCAM", "HAX", "BOT", "TOR", "DOX", "RAT", "HACK")),
        GORE(objectSetOf("BLOD", "GORE", "GUTS", "AMPT", "GASH", "SLSH", "SKIN")),
        PRE_BLOCKED(
            objectSetOf(
                "GHG", "XYZ", "AAA", "PISS", "LGBT", "TEAM", "CAST", "PEDO", "QWE", "MAMA",
                "PAPA", "MUM", "DAD", "MUMY", "DADY", "TOD", "GRWM", "BONG", "RUSS", "RUS",
                "MUSK", "ROMA", "TEST", "MÜLL", "AFK", "MOD", "DEV", "HELP", "SCAM"
            )
        ),
        PARTIES(
            objectSetOf(
                "CDU", "SPD", "FDP", "CSU", "GRN", "LNK", "GOP", "DEM", "LAB",
                "CON", "UKP", "BREX", "M5S", "LEG", "SNP", "BSW", "AFD", "3WEG"
            )
        ),
        ABLEISM(objectSetOf("TARD", "GIMP", "SPAZ", "SPST", "KRPL")),
        CHILD_ABUSE(objectSetOf("CHAB", "CHLD", "MINR", "PHIL")),
        BESTIALITY(objectSetOf("ZOOP", "BEAS", "ANML")),
        INCEST(objectSetOf("INCE")),
        XENOPHOBIA(objectSetOf("FORE", "XENO")),
        MAFIA(objectSetOf("MOB", "MFA", "COSA")),
        CARTELS(objectSetOf("CDG", "ZETA", "SICR"))
    }
}