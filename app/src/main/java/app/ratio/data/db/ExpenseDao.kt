package app.ratio.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.ratio.data.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Tu BookDao pero sin impl: Room genera el código (hace de DAO + JpaRepository).
 * - `suspend` = se ejecuta fuera del hilo UI (como @Async, pero obligatorio aquí).
 * - `Flow` = lista viva: la UI se actualiza sola al insertar (tu List<Book> era foto fija).
 * - Totales SIEMPRE por query SUM (neto.md §14), nunca cacheados.
 */
@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun findById(id: Long): Expense?

    /** Historial del mes, orden cronológico inverso. month = "YYYY-MM". */
    @Query(
        "SELECT * FROM expenses " +
            "WHERE strftime('%Y-%m', date / 1000, 'unixepoch', 'localtime') = :month " +
            "ORDER BY date DESC"
    )
    fun observeByMonth(month: String): Flow<List<Expense>>

    @Query(
        "SELECT * FROM expenses WHERE categoryId = :categoryId " +
            "AND strftime('%Y-%m', date / 1000, 'unixepoch', 'localtime') = :month " +
            "ORDER BY date DESC"
    )
    fun observeByCategoryAndMonth(categoryId: String, month: String): Flow<List<Expense>>

    /** Total mensual en céntimos. Null si no hay gastos → el repo lo convierte a 0. */
    @Query(
        "SELECT SUM(amountCents) FROM expenses " +
            "WHERE strftime('%Y-%m', date / 1000, 'unixepoch', 'localtime') = :month"
    )
    fun observeTotalByMonth(month: String): Flow<Long?>

    @Query(
        "SELECT SUM(amountCents) FROM expenses " +
            "WHERE categoryId = :categoryId " +
            "AND strftime('%Y-%m', date / 1000, 'unixepoch', 'localtime') = :month"
    )
    fun observeTotalByCategoryAndMonth(categoryId: String, month: String): Flow<Long?>
}
