package com.gameverse.app.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun String?.formatDate(separator: Char = ' '): String {
    if (isNullOrBlank()) return "-"
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

fun String?.formatDateTime(separator: Char = ' '): String {
    if (isNullOrBlank()) return "-"

    val parseDate = LocalDateTime.parse(this)

    val customFormat = LocalDateTime.Format {
        day()
        char(separator)
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(separator)
        year()
        chars(", ")
        hour()
        char(':')
        minute()
    }
    return parseDate.format(customFormat)
}

fun Int.toFormattedNumber(): String = toString()
    .reversed()
    .chunked(3)
    .joinToString(",")
    .reversed()

fun String.trimAfterDoubleNewline(): String? =
    Regex("\\n\\s*\\n").split(this, limit = 2).firstOrNull()
