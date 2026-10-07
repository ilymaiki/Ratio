package app.ratio.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedTest {

    @Test
    fun `hay 7 tags predefinidos`() {
        assertEquals(7, Seed.predefined.size)
    }

    @Test
    fun `ids unicos y todos predefinidos`() {
        val ids = Seed.predefined.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertTrue(Seed.predefined.all { it.isPredefined })
    }

    @Test
    fun `limite custom gratis es 3`() {
        assertEquals(3, Seed.MAX_CUSTOM_FREE)
    }
}
