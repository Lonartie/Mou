package com.lonartie.bookdiary.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lonartie.bookdiary.data.model.Book
import com.lonartie.bookdiary.data.model.ImageLinks
import com.lonartie.bookdiary.data.model.VolumeInfo

@Entity
data class BookData(
    @PrimaryKey val id: String,
    val title: String?,
    val authors: String?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val categories: String?,
    val smallThumbnail: String?,
    val thumbnail: String?,
    val addedDate: String?
) {
    companion object {
        fun fromBook(book: Book): BookData {
            return BookData(
                id = book.id,
                authors = book.volumeInfo.authors?.joinToString(",").orEmpty(),
                title = book.volumeInfo.title,
                publisher = book.volumeInfo.publisher,
                publishedDate = book.volumeInfo.publishedDate,
                description = book.volumeInfo.description,
                pageCount = book.volumeInfo.pageCount,
                categories = book.volumeInfo.categories?.joinToString(",").orEmpty(),
                smallThumbnail = book.volumeInfo.imageLinks?.smallThumbnail,
                thumbnail = book.volumeInfo.imageLinks?.thumbnail,
                addedDate = book.addedDate
            )
        }
    }

    fun toBook(): Book {
        return Book(
            id = id,
            volumeInfo = VolumeInfo(
                authors = authors?.split(",").orEmpty(),
                title = title,
                publisher = publisher,
                publishedDate = publishedDate,
                description = description,
                pageCount = pageCount,
                categories = categories?.split(",").orEmpty(),
                imageLinks = ImageLinks(
                    smallThumbnail = smallThumbnail,
                    thumbnail = thumbnail
                )
            ),
            addedDate = addedDate
        )
    }
}