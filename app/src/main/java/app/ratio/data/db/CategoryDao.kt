package app.ratio.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.ratio.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories WHERE isHidden = 0 ORDER BY name")
    fun observeVisible(): Flow<List<Category>>

    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun findAll(): List<Category>

    @Query("SELECT COUNT(*) FROM categories WHERE isPredefined = 0")
    suspend fun countCustom(): Int

    @Query("UPDATE categories SET isHidden = :hidden WHERE id = :id")
    suspend fun setHidden(id: String, hidden: Boolean)

    @Query("DELETE FROM categories WHERE id = :id AND isPredefined = 0")
    suspend fun deleteCustomById(id: String)
}
