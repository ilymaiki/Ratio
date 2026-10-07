package app.ratio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Etiqueta de gasto. Las 7 predefinidas salen de Seed.kt (neto.md §5.1).
 * - Predefinida: no se borra, solo se oculta (isHidden).
 * - Personalizada: creada por el usuario (máx 3 gratis en MVP).
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    /** Color ARGB como Long, ej: 0xFF4CAF50 */
    val color: Long,
    /** Nombre del icono Material, ej: "home", "food". Ver ui/theme/Tags.kt (futuro). */
    val icon: String,
    val isPredefined: Boolean = true,
    val isHidden: Boolean = false
)
