package app.ratio.ui.home

import app.cash.turbine.test
import app.ratio.data.db.CategoryDao
import app.ratio.data.db.ExpenseDao
import app.ratio.data.model.Category
import app.ratio.data.repo.CategoryRepo
import app.ratio.data.repo.ExpenseRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val expenseDao: ExpenseDao = mockk()
    private val categoryDao: CategoryDao = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emite total y totales por categoria`() = runTest {
        val cats = listOf(
            Category("comida", "Comida", 0, "food"),
            Category("piso", "Piso", 0, "home")
        )
        coEvery { categoryDao.observeVisible() } returns flowOf(cats)
        coEvery { expenseDao.observeTotalByMonth("2026-10") } returns flowOf(9000L)
        coEvery { expenseDao.observeTotalByCategoryAndMonth("comida", "2026-10") } returns flowOf(7000L)
        coEvery { expenseDao.observeTotalByCategoryAndMonth("piso", "2026-10") } returns flowOf(2000L)

        val vm = HomeViewModel(
            ExpenseRepo(expenseDao),
            CategoryRepo(categoryDao),
            month = "2026-10"
        )
        vm.uiState.test {
            val state = awaitItem()
            assertEquals(9000L, state.totalCents)
            assertEquals(2, state.categories.size)
            assertEquals(7000L, state.categories[0].totalCents)
            assertEquals(2000L, state.categories[1].totalCents)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
