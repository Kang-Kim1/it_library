package com.example.kanglibrary.network

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.view.BookListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Handler

/**
 * @file HTTPRequestAsyncTask.kt
 * @date 09/09/2015
 * @brief  AsyncTask class to handle HTTP request for BOOK DETAILS
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListRequestAsyncTask(val progressBar : ProgressBar, val parentActivity : AppCompatActivity) : AsyncTask<Void, Integer, List<Book>>() {
    var result : List<Book> = ArrayList<Book>()
    var resultList = ArrayList<Book>()
    var isDone : Boolean = false;
    val retrofit = RetrofitClient.getInstance()
    val api = retrofit.create(RetrofitService::class.java)
    var bookCount : Int = 0

    fun  getAllBooks(query : String, page : Int) {
        val call = api.getAllBooks(query ,page)
        Log.d(this.javaClass.name,"getAllBooks ")
        call.enqueue(object : Callback<BookSearchResult> {
            override fun onResponse(
                call: Call<BookSearchResult>,
                response: Response<BookSearchResult>
            ) {
                Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response}")
                Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response.body()}")

                if(page == 1) {
                    bookCount = response.body()?.total!!.toInt()
                    markBookCount(bookCount)
                }

                val books = response.body()?.books as ArrayList<Book>

                Log.d(this.javaClass.name, "getAllBooks > onResponse > List Count :  ${books.size} / ${page}")
                resultList.addAll(books)
//                if(total / 10 > page) {
//                    getAllBooks(query,  page + 1)
//                } else {
//                    Log.d(this.javaClass.name, "getAllBooks > onResponse > Preparing details from now...")
//                    isDone = true
//                    return
//                }
            }

            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBooks > onFailure > message : ${t.message}")

            }
        })
    }

    private fun markBookCount(bookCount: Int) {
        this.bookCount = bookCount

    }

    override fun doInBackground(vararg p0: Void?): List<Book> {

        val requestHelper = RequestHelper()
//        requestHelper.getAllBooks("android",1)
        val query = "android"
        var page = 1
        getAllBooks("android", page)

//        while(!(bookCount == page)) {
//            Log.d("TOTAL SIZE ", "${bookCount}")
//        }

        while(!(page == bookCount)) {
            getAllBooks(query, ++page)
            Log.d("TOTAL SIZE ", "${bookCount}")
        }

//        while(!isDone) { Log.d("LOADING : ", "LOADING!!!!") }

        return requestHelper.getResult()
    }

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d(this.javaClass.name,"OnPreExecute")
        progressBar.visibility = View.VISIBLE
    }

    override fun onPostExecute(result: List<Book>?) {
        super.onPostExecute(result)
        this.result = result!!
        progressBar.visibility = View.INVISIBLE

        // Start List by passing result list
        val intent = Intent(parentActivity, BookListActivity::class.java)
        intent!!.putParcelableArrayListExtra("BOOK_LIST", result as ArrayList<Book>)
        Log.d(this.javaClass.name,"OnPostExecute")

        parentActivity.startActivity(intent)
    }

}