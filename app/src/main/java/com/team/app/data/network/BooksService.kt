package com.lonartie.bookdiary.data.network

import com.lonartie.bookdiary.data.model.BooksList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksService {

    @GET("volumes")
    suspend fun searchBook(
        @Query("q") name: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int
    ): Response<BooksList?>

}