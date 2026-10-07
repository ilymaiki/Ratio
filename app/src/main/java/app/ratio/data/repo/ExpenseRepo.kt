package app.ratio.data.repo

import app.ratio.data.db.ExpenseDao
import app.ratio.data.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Tu BookService pero sin interfaz/impl: una clase basta en MVP.
 * - Valida antes de guardar (cantidad > 0).
 * - Convierte el SUM null (sin gastos) en 0 para que la UI no trate nulls.
 */
class ExpenseRepo(private val dao: ExpenseDao) {

    fun observeByMonth(month: String): Flow<List<Expense>> =
        dao.observeByMonth(month)

    fun observeTotalByMonth(month: String): Flow<Long> =
        dao.observeTotalByMonth(month).map { it ?: 0L }

    fun observeTotalByCategory(categoryId: String, month: String): Flow<Long> =
        dao.observeTotalByCategoryAndMonth(categoryId, month).map { it ?: 0L }

    suspend fun save(expense: Expense): Long {
        require(expense.amountCents > 0) { "La cantidad debe ser mayor que 0" }
        require(expense.categoryId.isNotBlank()) { "Falta la etiqueta" }
        return dao.insert(expense)
    }

    suspend fun update(expense: Expense) {
        require(expense.amountCents > 0) { "La cantidad debe ser mayor que 0" }
        dao.update(expense)
    }

    suspend fun delete(expense: Expense) = dao.delete(expense)

    suspend fun findById(id: Long): Expense? = dao.findById(id)
}
