package app.ratio.data.repo

import app.ratio.data.db.CategoryDao
import app.ratio.domain.Seed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryRepoTest {

    private val dao: CategoryDao = mockk(relaxed = true)
    private val repo = CategoryRepo(dao)

    @Test
    fun `seed inserta los 7 predefinidos`() = runTest {
        repo.ensureSeeded()
        coVerify { dao.insertAll(Seed.predefined) }
    }

    @Test
    fun `cuarto tag propio es rechazado`() = runTest {
        coEvery { dao.countCustom() } returns 3
        try {
            repo.createCustom("Gym", 0xFF000000, "gym")
            throw AssertionError("debería haber lanzado")
        } catch (e: IllegalStateException) {
            assertEquals(3, Seed.MAX_CUSTOM_FREE)
        }
        coVerify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `tercer tag propio se guarda`() = runTest {
        coEvery { dao.countCustom() } returns 2
        coEvery { dao.insert(any()) } returns Unit
        val created = repo.createCustom("Gym", 0xFF000000, "gym")
        assertEquals("Gym", created.name)
        coVerify { dao.insert(created) }
    }
}
