package dev.slne.clan.core.utils.tag

import dev.slne.clan.api.tag.ClanTagColor

private const val negSpaceChar = "ꑛ"

open class ClanTagColorMap(
    val clanTagColor: ClanTagColor,
    val a: String,
    val b: String,
    val c: String,
    val d: String,
    val e: String,
    val f: String,
    val g: String,
    val h: String,
    val i: String,
    val j: String,
    val k: String,
    val l: String,
    val m: String,
    val n: String,
    val o: String,
    val p: String,
    val q: String,
    val r: String,
    val s: String,
    val t: String,
    val u: String,
    val v: String,
    val w: String,
    val x: String,
    val y: String,
    val z: String,
    val zero: String,
    val one: String,
    val two: String,
    val three: String,
    val four: String,
    val five: String,
    val six: String,
    val seven: String,
    val eight: String,
    val nine: String,
    val spacerOne: String,
    val spacerTwo: String,
) {
    private val translatedLettersToGlyph = mapOf(
        "a" to a,
        "b" to b,
        "c" to c,
        "d" to d,
        "e" to e,
        "f" to f,
        "g" to g,
        "h" to h,
        "i" to i,
        "j" to j,
        "k" to k,
        "l" to l,
        "m" to m,
        "n" to n,
        "o" to o,
        "p" to p,
        "q" to q,
        "r" to r,
        "s" to s,
        "t" to t,
        "u" to u,
        "v" to v,
        "w" to w,
        "x" to x,
        "y" to y,
        "z" to z,
        "0" to zero,
        "1" to one,
        "2" to two,
        "3" to three,
        "4" to four,
        "5" to five,
        "6" to six,
        "7" to seven,
        "8" to eight,
        "9" to nine,
    )

    private fun translateToGlyphs(input: String) =
        input.map { translatedLettersToGlyph[it.toString().lowercase()] ?: it }.toList()

    fun translateClanTag(input: String): String {
        val translated = translateToGlyphs(input)

        return "$spacerTwo$negSpaceChar${translated.joinToString(negSpaceChar)}$negSpaceChar$spacerOne"
    }
}
