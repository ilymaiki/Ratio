package app.ratio.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class MonthTest {

    @Test
    fun `formatea epoch a YYYY-MM`() {
        val epoch = LocalDate.of(2026, 10, 7)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        assertEquals("2026-10", Month.format(epoch))
    }

    @Test
    fun `mes con cero delante`() {
        val epoch = LocalDate.of(2026, 2, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        assertEquals("2026-02", Month.format(epoch))
    }
}
