package app.ratio.domain

import java.text.NumberFormat
import java.util.Locale

/** Dinero en céntimos → texto es-ES con € fijo (neto.md §14). 1250 → "12,50 €". */
object Money {

    private val format: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("es", "ES"))

    fun format(cents: Long): String = format.format(cents / 100.0)

    /** "12,50" → 1250. Acepta coma o punto. Lanza si no es número positivo. */
    fun parseToCents(input: String): Long {
        val normalized = input.trim().replace(".", "").replace(",", ".")
        val value = normalized.toDoubleOrNull()
            ?: throw IllegalArgumentException("Cantidad no válida")
        if (value <= 0) throw IllegalArgumentException("La cantidad debe ser mayor que 0")
        return (value * 100).toLong()
    }
}
