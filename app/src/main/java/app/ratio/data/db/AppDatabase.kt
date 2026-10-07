package app.ratio.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.ratio.data.model.Budget
import app.ratio.data.model.Category
import app.ratio.data.model.Expense

/**
 * La "base de datos". Sustituye a tu persistence/impl + JpaRepository:
 * Room genera la implementación de los 3 DAOs al compilar (vía KSP).
 */
@Database(
    entities = [Expense::class, Category::class, Budget::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
}
