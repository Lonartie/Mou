package com.lonartie.bookdiary.data.repositories

import com.lonartie.bookdiary.data.database.BooksDao
import com.lonartie.bookdiary.data.database.model.BookData
import com.lonartie.bookdiary.data.model.Book
import com.lonartie.bookdiary.data.network.BooksService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class BooksRepository(private val api: BooksService, private val dao: BooksDao) {

    suspend fun searchBook(name: String, startIndex: Int = 0, maxResults: Int = 5): List<Book> {
        val response = api.searchBook("intitle:${name}", startIndex, maxResults)
        if (!response.isSuccessful) return emptyList()
        val result = response.body()
        if (result == null) return emptyList()
        return result.items
    }

    suspend fun addBookToList(book: Book) {
        val formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withZone(ZoneId.systemDefault())
        val nowString = formatter.format(Instant.now())

        val newBook = Book(
            id = book.id,
            volumeInfo = book.volumeInfo,
            addedDate = nowString
        )
        dao.insert(BookData.fromBook(newBook))
    }

    suspend fun getAllBooks(): List<Book> {
        val booksList = dao.getBooks()
        return booksList.map { it.toBook() }.toList()
    }

    suspend fun removeBook(book: Book) {
        dao.delete(BookData.fromBook(book))
    }

}