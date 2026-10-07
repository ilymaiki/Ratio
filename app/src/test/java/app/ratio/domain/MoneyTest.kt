package app.ratio.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MoneyTest {

    @Test
    fun `1250 céntimos contienen 12 y 50`() {
        val text = Money.format(1250)
        assertTrue(text.contains("12"))
        assertTrue(text.contains("50"))
        assertTrue(text.contains("€"))
    }

    @Test
    fun `parsea coma decimal`() {
        assertEquals(1250L, Money.parseToCents("12,50"))
    }

    @Test
    fun `parsea punto decimal`() {
        assertEquals(1250L, Money.parseToCents("12.50"))
    }

    @Test
    fun `rechaza cero y texto`() {
        try {
            Money.parseToCents("0")
            throw AssertionError("debería lanzar")
        } catch (e: IllegalArgumentException) { }
        try {
            Money.parseToCents("abc")
            throw AssertionError("debería lanzar")
        } catch (e: IllegalArgumentException) { }
    }
}
