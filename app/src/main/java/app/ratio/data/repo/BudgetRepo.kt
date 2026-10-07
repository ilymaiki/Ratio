package app.ratio.data.repo

import app.ratio.data.db.BudgetDao
import app.ratio.data.model.Budget
import kotlinx.coroutines.flow.Flow

class BudgetRepo(private val dao: BudgetDao) {

    fun observeBudget(categoryId: String, month: String): Flow<Budget?> =
        dao.observeBudget(categoryId, month)

    suspend fun setLimit(categoryId: String, month: String, limitCents: Long) {
        require(limitCents > 0) { "El límite debe ser mayor que 0" }
        dao.upsert(Budget(categoryId, month, limitCents))
    }

    /**
     * Día 1 de mes: el gastado sale solo de las queries del mes nuevo (= 0),
     * aquí solo se heredan los límites del mes anterior si el mes nuevo
     * aún no tiene ninguno (neto.md §7).
     */
    suspend fun carryOverLimits(fromMonth: String, toMonth: String) {
        if (dao.findByMonth(toMonth).isNotEmpty()) return
        dao.findByMonth(fromMonth).forEach { dao.upsert(it.copy(month = toMonth)) }
    }
}
