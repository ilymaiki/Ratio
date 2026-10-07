package app.ratio.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class BudgetRulesTest {

    @Test
    fun `sin limite siempre OK`() {
        assertEquals(BudgetStatus.OK, BudgetRules.status(99999, null))
    }

    @Test
    fun `menos de 80 es OK`() {
        assertEquals(BudgetStatus.OK, BudgetRules.status(79, 100))
    }

    @Test
    fun `80 es aviso`() {
        assertEquals(BudgetStatus.WARNING, BudgetRules.status(80, 100))
    }

    @Test
    fun `100 es rojo`() {
        assertEquals(BudgetStatus.OVER, BudgetRules.status(100, 100))
    }

    @Test
    fun `ejemplo neto 90 de 100`() {
        assertEquals(90, BudgetRules.percent(90, 100))
        assertEquals(10L, BudgetRules.freeCents(90, 100))
        assertEquals(BudgetStatus.WARNING, BudgetRules.status(90, 100))
    }
}
