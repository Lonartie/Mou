package com.lonartie.bookdiary.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lonartie.bookdiary.data.database.model.BookData

@Database(entities = [BookData::class], version = 3)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookdata ADD COLUMN authors TEXT")
                db.execSQL("ALTER TABLE bookdata ADD COLUMN categories TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookdata ADD COLUMN addedDate TEXT")
            }
        }
    }
}