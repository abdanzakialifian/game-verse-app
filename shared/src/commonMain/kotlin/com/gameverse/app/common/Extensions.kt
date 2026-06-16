package com.gameverse.app.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun String.formatDate(separator: Char = ' '): String {
    if (isBlank()) return "-"
    val parseDate = LocalDate.parse(this)
    val customFormat = LocalDate.Format {
        day()
        char(separator)
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(separator)
        year()
    }
    return parseDate.format(customFormat)
}

fun Int.toFormattedNumber(): String = toString()
    .reversed()
    .chunked(3)
    .joinToString(",")
    .reversed()
