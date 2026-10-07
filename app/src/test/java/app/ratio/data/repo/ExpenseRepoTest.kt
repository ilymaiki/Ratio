package app.ratio.data.repo

import app.cash.turbine.test
import app.ratio.data.db.ExpenseDao
import app.ratio.data.model.Expense
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpenseRepoTest {

    private val dao: ExpenseDao = mockk()
    private val repo = ExpenseRepo(dao)

    @Test
    fun `guardar rechaza cantidad cero sin tocar BD`() = runTest {
        try {
            repo.save(Expense(amountCents = 0, categoryId = "comida"))
            throw AssertionError("debería haber lanzado")
        } catch (e: IllegalArgumentException) {
            // esperado
        }
        coVerify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `guardar delega al dao si es valido`() = runTest {
        val expense = Expense(amountCents = 1250, categoryId = "comida")
        coEvery { dao.insert(expense) } returns 1L
        assertEquals(1L, repo.save(expense))
        coVerify { dao.insert(expense) }
    }

    @Test
    fun `total sin gastos es 0 y no null`() = runTest {
        coEvery { dao.observeTotalByMonth("2026-10") } returns flowOf(null)
        repo.observeTotalByMonth("2026-10").test {
            assertEquals(0L, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `total con gastos pasa el valor`() = runTest {
        coEvery { dao.observeTotalByMonth("2026-10") } returns flowOf(9000L)
        repo.observeTotalByMonth("2026-10").test {
            assertEquals(9000L, awaitItem())
            awaitComplete()
        }
    }
}
