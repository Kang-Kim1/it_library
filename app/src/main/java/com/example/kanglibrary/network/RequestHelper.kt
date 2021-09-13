package com.example.kanglibrary.network

import android.util.Log
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.concurrent.thread


/**
 * @file RequestHelper.kt
 * @date 09/09/2021
 * @brief Helper class for HTTP request using Retrofit
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class RequestHelper {

    var resultList = ArrayList<Book>()

    var bookCount = 0

    fun  getAllBooks(query : String, page : Int, size : Int) {
       val retrofit = RetrofitClient.getInstance()
       val api = retrofit.create(RetrofitService::class.java)
       val call = api.getAllBooks(query, page)

       val response = call.execute()
       if (response.isSuccessful) {
           Log.d(this.javaClass.name, "getAllBooks > onSuccess")

           Log.d(this.javaClass.name, "getAllBookDetails > onResponse > ${response}")

           if (page == 1) {
               bookCount = response.body()?.total!!.toInt()
               Log.d("BOOKCOUNT : ", "${bookCount}")
           }

           // If there are more books left to get
           resultList.addAll(response.body()?.books as List<Book>)

       } else {
           Log.d(this.javaClass.name, "getAllBookDetails > onFailure > ${response}")
       }
    }

    fun getSize() : Int{
        return bookCount
    }

    fun getBookDetail(book : Book) : Book {
        lateinit var bookDetail : Book
        val retrofit = RetrofitClient.getInstance()
        val api = retrofit.create(RetrofitService::class.java)
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