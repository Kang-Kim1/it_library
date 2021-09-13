package com.example.kanglibrary.network

import android.util.Log
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @file RequestHelper.kt
 * @date 09/09/2021
 * @brief Helper class for HTTP request using Retrofit
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class RequestHelper {
    var resultList = ArrayList<Book>()
    var isDone : Boolean = false;
    val retrofit = RetrofitClient.getInstance()
    val api = retrofit.create(RetrofitService::class.java)

    fun  getAllBooks(query : String, page : Int) {
        val call = api.getAllBooks(query ,page)
        Log.d(this.javaClass.name,"getAllBooks")
        call.enqueue(object : Callback<BookSearchResult> {
            override fun onResponse(
                    call: Call<BookSearchResult>,
                    response: Response<BookSearchResult>
            ) {
                Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response}")
                Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response.body()}")

                val total = response.body()?.total!!.toInt()
                val books = response.body()?.books as ArrayList<Book>

                Log.d(this.javaClass.name, "getAllBooks > onResponse > List Count :  ${books.size} / ${page}")
                resultList.addAll(books)

                if(total / 10 > page) {
                    getAllBooks(query,  page + 1)
                } else {
                    Log.d(this.javaClass.name, "getAllBooks > onResponse > Preparing details from now...")
                    isDone = true
                    return
                }
            }

            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBooks > onFailure > message : ${t.message}")

            }
        })
    }

    fun getBookDetail(book : Book) : Book {
        lateinit var bookDetail : Book
        val call = api.getBookDetail(book.isbn13!!)
        call.enqueue(object : Callback<Book> {
            override fun onResponse(
                call: Call<Book>,
                response: Response<Book>
            ) {
                Log.d(this.javaClass.name, "getAllBookDetails > onResponse > ${response}")

                bookDetail = response.body() as Book
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBookDetails > onFailure > message / isbn : ${t.message} / ${book.isbn13}")
            }
        })
        return bookDetail
    }

    fun getResult(): ArrayList<Book> {
        return resultList
    }
}