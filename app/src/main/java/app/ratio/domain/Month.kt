package app.ratio.domain

import java.time.LocalDate
import java.time.ZoneId

/** Mes en formato "YYYY-MM" para agrupar gastos y presupuestos (neto.md §13). */
object Month {

    fun current(): String = format(System.currentTimeMillis())

    fun format(epochMillis: Long): String {
        val date = LocalDate.ofInstant(
            java.time.Instant.ofEpochMilli(epochMillis),
            ZoneId.systemDefault()
        )
        return "%04d-%02d".format(date.year, date.monthValue)
    }
}
