package com.wzlibs.localehelper

import android.content.Context
import java.util.Locale

object Locales {
    val Afrikaans: Locale by lazy { Locale("af", "ZA") }
    val Albanian: Locale by lazy { Locale("sq", "AL") }
    val Arabic: Locale by lazy { Locale("ar", "SA") }
    val Armenian: Locale by lazy { Locale("hy", "AM") }
    val Belarus: Locale by lazy { Locale("be", "BY") }
    val Bulgarian: Locale by lazy { Locale("bg", "BG") }
    val Danish: Locale by lazy { Locale("da", "DK") }
    val Dutch: Locale by lazy { Locale("nl", "NL") }
    val English: Locale by lazy { Locale("en", "US") }
    val Estonian: Locale by lazy { Locale("et", "EE") }
    val Filipino: Locale by lazy { Locale("fil", "PH") }
    val Finnish: Locale by lazy { Locale("fi", "FI") }
    val French: Locale by lazy { Locale("fr", "FR") }
    val Georgian: Locale by lazy { Locale("ka", "GE") }
    val German: Locale by lazy { Locale("de", "DE") }
    val Greek: Locale by lazy { Locale("el", "GR") }
    val Hawaiian: Locale by lazy { Locale("haw", "US") }
    val Hebrew: Locale by lazy { Locale("he", "IL") }
    val Hindi: Locale by lazy { Locale("hi", "IN") }
    val Hungarian: Locale by lazy { Locale("hu", "HU") }
    val Icelandic: Locale by lazy { Locale("is", "IS") }
    val Indonesian: Locale by lazy { Locale("id", "ID") }
    val Irish: Locale by lazy { Locale("ga", "IE") }
    val Italian: Locale by lazy { Locale("it", "IT") }
    val Japanese: Locale by lazy { Locale("ja", "JP") }
    val Korean: Locale by lazy { Locale("ko", "KR") }
    val Latvian: Locale by lazy { Locale("lv", "LV") }
    val Lithuanian: Locale by lazy { Locale("lt", "LT") }
    val Luo: Locale by lazy { Locale("luo", "KE") }
    val Macedonian: Locale by lazy { Locale("mk", "MK") }
    val Malagasy: Locale by lazy { Locale("mg", "MG") }
    val Malay: Locale by lazy { Locale("ms", "MY") }
    val Nepali: Locale by lazy { Locale("ne", "NP") }
    val NorwegianBokmal: Locale by lazy { Locale("nb", "NO") }
    val NorwegianNynorsk: Locale by lazy { Locale("nn", "NO") }
    val Persian: Locale by lazy { Locale("fa", "IR") }
    val Polish: Locale by lazy { Locale("pl", "PL") }
    val Portuguese: Locale by lazy { Locale("pt", "PT") }
    val Romanian: Locale by lazy { Locale("ro", "RO") }
    val Russian: Locale by lazy { Locale("ru", "RU") }
    val Slovak: Locale by lazy { Locale("sk", "SK") }
    val Slovenian: Locale by lazy { Locale("sl", "SI") }
    val Spanish: Locale by lazy { Locale("es", "ES") }
    val Swedish: Locale by lazy { Locale("sv", "SE") }
    val Thai: Locale by lazy { Locale("th", "TH") }
    val Turkish: Locale by lazy { Locale("tr", "TR") }
    val Ukrainian: Locale by lazy { Locale("uk", "UA") }
    val Urdu: Locale by lazy { Locale("ur", "IN") }
    val Vietnamese: Locale by lazy { Locale("vi", "VN") }
    val Zulu: Locale by lazy { Locale("zu", "ZA") }

    /**
     * List of language codes for Right-To-Left languages
     */
    val RTL: Set<String> by lazy {
        hashSetOf(
            "ar",
            "dv",
            "fa",
            "ha",
            "he",
            "iw",
            "ji",
            "ps",
            "sd",
            "ug",
            "ur",
            "yi"
        )
    }

    fun getWrapLocales(context: Context): List<WrapLocale> {
        return listOf(
            WrapLocale(1, FlagKit.getResId(context, Afrikaans.country), Afrikaans),
            WrapLocale(2, FlagKit.getResId(context, Albanian.country), Albanian),
            WrapLocale(3, FlagKit.getResId(context, Arabic.country), Arabic),
            WrapLocale(4, FlagKit.getResId(context, Armenian.country), Armenian),
            WrapLocale(5, FlagKit.getResId(context, Belarus.country), Belarus),
            WrapLocale(6, FlagKit.getResId(context, Bulgarian.country), Bulgarian),
            WrapLocale(7, FlagKit.getResId(context, Danish.country), Danish),
            WrapLocale(8, FlagKit.getResId(context, Dutch.country), Dutch),
            WrapLocale(9, FlagKit.getResId(context, English.country), English),
            WrapLocale(10, FlagKit.getResId(context, Estonian.country), Estonian),
            WrapLocale(11, FlagKit.getResId(context, Filipino.country), Filipino),
            WrapLocale(12, FlagKit.getResId(context, Finnish.country), Finnish),
            WrapLocale(13, FlagKit.getResId(context, French.country), French),
            WrapLocale(14, FlagKit.getResId(context, Georgian.country), Georgian),
            WrapLocale(15, FlagKit.getResId(context, German.country), German),
            WrapLocale(16, FlagKit.getResId(context, Greek.country), Greek),
            WrapLocale(17, FlagKit.getResId(context, Hawaiian.country), Hawaiian),
            WrapLocale(18, FlagKit.getResId(context, Hebrew.country), Hebrew),
            WrapLocale(19, FlagKit.getResId(context, Hindi.country), Hindi),
            WrapLocale(20, FlagKit.getResId(context, Hungarian.country), Hungarian),
            WrapLocale(21, FlagKit.getResId(context, Icelandic.country), Icelandic),
            WrapLocale(22, FlagKit.getResId(context, Indonesian.country), Indonesian),
            WrapLocale(23, FlagKit.getResId(context, Irish.country), Irish),
            WrapLocale(24, FlagKit.getResId(context, Italian.country), Italian),
            WrapLocale(25, FlagKit.getResId(context, Japanese.country), Japanese),
            WrapLocale(26, FlagKit.getResId(context, Korean.country), Korean),
            WrapLocale(27, FlagKit.getResId(context, Latvian.country), Latvian),
            WrapLocale(28, FlagKit.getResId(context, Lithuanian.country), Lithuanian),
            WrapLocale(29, FlagKit.getResId(context, Luo.country), Luo),
            WrapLocale(30, FlagKit.getResId(context, Macedonian.country), Macedonian),
            WrapLocale(31, FlagKit.getResId(context, Malagasy.country), Malagasy),
            WrapLocale(32, FlagKit.getResId(context, Malay.country), Malay),
            WrapLocale(33, FlagKit.getResId(context, Nepali.country), Nepali),
            WrapLocale(34, FlagKit.getResId(context, NorwegianBokmal.country), NorwegianBokmal),
            WrapLocale(35, FlagKit.getResId(context, NorwegianNynorsk.country), NorwegianNynorsk),
            WrapLocale(36, FlagKit.getResId(context, Persian.country), Persian),
            WrapLocale(37, FlagKit.getResId(context, Polish.country), Polish),
            WrapLocale(38, FlagKit.getResId(context, Portuguese.country), Portuguese),
            WrapLocale(39, FlagKit.getResId(context, Romanian.country), Romanian),
            WrapLocale(40, FlagKit.getResId(context, Russian.country), Russian),
            WrapLocale(41, FlagKit.getResId(context, Slovak.country), Slovak),
            WrapLocale(42, FlagKit.getResId(context, Slovenian.country), Slovenian),
            WrapLocale(43, FlagKit.getResId(context, Spanish.country), Spanish),
            WrapLocale(44, FlagKit.getResId(context, Swedish.country), Swedish),
            WrapLocale(45, FlagKit.getResId(context, Thai.country), Thai),
            WrapLocale(46, FlagKit.getResId(context, Turkish.country), Turkish),
            WrapLocale(47, FlagKit.getResId(context, Ukrainian.country), Ukrainian),
            WrapLocale(48, FlagKit.getResId(context, Urdu.country), Urdu),
            WrapLocale(49, FlagKit.getResId(context, Vietnamese.country), Vietnamese),
            WrapLocale(50, FlagKit.getResId(context, Zulu.country), Zulu),
        )
    }

}
