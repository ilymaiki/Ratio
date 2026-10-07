package app.ratio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Un gasto. Equivale a tu Book.java (@Entity) pero en Kotlin:
 * - `data class` genera equals/hashCode/getters automáticamente.
 * - `val` = final (como tus fields con solo getter).
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** Céntimos, nunca Float (neto.md §13). 12,50€ = 1250 */
    val amountCents: Long,
    val categoryId: String,
    /** Epoch millis. Por defecto: hoy. */
    val date: Long = System.currentTimeMillis(),
    /** Nota libre, 140 car. máx (neto.md §6). Null = sin nota. */
    val note: String? = null,
    /** Ruta en app_data/. Null = sin foto (neto.md §6). */
    val photoPath: String? = null
)
