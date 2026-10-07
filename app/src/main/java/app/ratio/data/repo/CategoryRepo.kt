package app.ratio.data.repo

import app.ratio.data.db.CategoryDao
import app.ratio.data.model.Category
import app.ratio.domain.Seed
import kotlinx.coroutines.flow.Flow

class CategoryRepo(private val dao: CategoryDao) {

    fun observeVisible(): Flow<List<Category>> = dao.observeVisible()

    /** Primera apertura: inserta los 7 predefinidos (IGNORE = sin duplicados). */
    suspend fun ensureSeeded() = dao.insertAll(Seed.predefined)

    /**
     * Crea un tag propio. Regla MVP (neto.md §5.2): máx 3 gratis.
     * Lanza IllegalStateException si se supera (la UI lo traduce a mensaje).
     */
    suspend fun createCustom(name: String, color: Long, icon: String): Category {
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        if (dao.countCustom() >= Seed.MAX_CUSTOM_FREE) {
            throw IllegalStateException("Límite de ${Seed.MAX_CUSTOM_FREE} etiquetas propias alcanzado")
        }
        val category = Category(
            id = "custom_${System.currentTimeMillis()}",
            name = name.trim(),
            color = color,
            icon = icon,
            isPredefined = false
        )
        dao.insert(category)
        return category
    }

    suspend fun setHidden(id: String, hidden: Boolean) = dao.setHidden(id, hidden)

    suspend fun deleteCustom(id: String) = dao.deleteCustomById(id)
}
