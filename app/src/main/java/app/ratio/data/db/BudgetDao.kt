package app.ratio.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.ratio.data.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month")
    fun observeBudget(categoryId: String, month: String): Flow<Budget?>

    @Query("SELECT * FROM budgets WHERE month = :month")
    suspend fun findByMonth(month: String): List<Budget>
}
