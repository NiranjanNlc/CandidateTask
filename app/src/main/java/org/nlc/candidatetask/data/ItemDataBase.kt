package org.nlc.candidatetask.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * FROM books")
    fun getAllItems(): List<Book>

    @Query("SELECT * FROM books WHERE isDeleted = 0")
    fun getNonDeletedItemBy(): List<Book>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getItemById(id: Int): Book
}

@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}