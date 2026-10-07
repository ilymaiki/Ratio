package app.ratio.data.repo

import app.ratio.data.db.BudgetDao
import app.ratio.data.model.Budget
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BudgetRepoTest {

    private val dao: BudgetDao = mockk(relaxed = true)
    private val repo = BudgetRepo(dao)

    @Test
    fun `limite cero es rechazado`() = runTest {
        try {
            repo.setLimit("transporte", "2026-10", 0)
            throw AssertionError("debería haber lanzado")
        } catch (e: IllegalArgumentException) {
            // esperado
        }
        coVerify(exactly = 0) { dao.upsert(any()) }
    }

    @Test
    fun `cambio de mes hereda limites si el mes nuevo esta vacio`() = runTest {
        coEvery { dao.findByMonth("2026-11") } returns emptyList()
        coEvery { dao.findByMonth("2026-10") } returns listOf(Budget("transporte", "2026-10", 5000))
        repo.carryOverLimits("2026-10", "2026-11")
        coVerify { dao.upsert(Budget("transporte", "2026-11", 5000)) }
    }

    @Test
    fun `cambio de mes no pisa limites ya definidos`() = runTest {
        coEvery { dao.findByMonth("2026-11") } returns listOf(Budget("transporte", "2026-11", 7000))
        repo.carryOverLimits("2026-10", "2026-11")
        coVerify(exactly = 0) { dao.upsert(any()) }
    }
}
