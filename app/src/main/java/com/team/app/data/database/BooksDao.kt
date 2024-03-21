package com.lonartie.bookdiary.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lonartie.bookdiary.data.database.model.BookData

@Dao
interface BooksDao {
    @Query("SELECT * FROM bookdata")
    suspend fun getBooks(): List<BookData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookData)

    @Delete
    suspend fun delete(book: BookData)

}