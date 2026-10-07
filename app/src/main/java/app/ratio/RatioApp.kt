package app.ratio

import android.app.Application
import androidx.room.Room
import app.ratio.data.db.AppDatabase
import app.ratio.data.repo.BudgetRepo
import app.ratio.data.repo.CategoryRepo
import app.ratio.data.repo.ExpenseRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Punto de arranque real (sustituye al main() de Spring).
 * Crea Room una sola vez y deja los repos a mano para las pantallas.
 * En la primera apertura inserta los 7 tags (Seed).
 */
class RatioApp : Application() {

    lateinit var db: AppDatabase
        private set
    lateinit var expenseRepo: ExpenseRepo
        private set
    lateinit var categoryRepo: CategoryRepo
        private set
    lateinit var budgetRepo: BudgetRepo
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDatabase::class.java, "ratio.db").build()
        expenseRepo = ExpenseRepo(db.expenseDao())
        categoryRepo = CategoryRepo(db.categoryDao())
        budgetRepo = BudgetRepo(db.budgetDao())
        scope.launch { categoryRepo.ensureSeeded() }
    }
}
