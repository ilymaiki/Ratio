package app.ratio.data.model

import androidx.room.Entity

/**
 * Límite mensual por categoría o global ("GLOBAL").
 * - month formato "YYYY-MM" (neto.md §13), ej: "2026-10".
 * - Día 1: el gastado se calcula a 0 (query del mes nuevo),
 *   el límite se conserva copiando la fila (lo hará BudgetRepo).
 */
@Entity(tableName = "budgets", primaryKeys = ["categoryId", "month"])
data class Budget(
    val categoryId: String,
    val month: String,
    val limitCents: Long
)
