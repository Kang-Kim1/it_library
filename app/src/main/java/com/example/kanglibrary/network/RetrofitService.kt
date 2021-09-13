package com.example.kanglibrary.network

import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("1.0/search/{query}/{page}")
    fun getAllBooks(
        @Path("query") query : String,
        @Path("page") page : Int
    ) : Call<BookSearchResult>

    @GET("https://api.itbook.store/1.0/books/{isbn}")
    fun getBookDetail(
        @Path("isbn") query : String
    ) : Call<Book>
}