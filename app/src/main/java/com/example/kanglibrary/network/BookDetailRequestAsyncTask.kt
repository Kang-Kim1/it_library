package com.example.kanglibrary.network

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.view.BookListActivity

/**
 * @file BookDetailRequestAsyncTask.kt
 * @date 13/09/2021
 * @brief AsyncTask class to handle HTTP request for BOOK DETAILS
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookDetailRequestAsyncTask(val progressBar : ProgressBar, val book : Book, val parentActivity : AppCompatActivity) : AsyncTask<Void, Integer, Book>() {

    override fun doInBackground(vararg p0: Void?): Book {
        val requestHelper = RequestHelper()
        val bookDetail = requestHelper.getBookDetail(book)

        // Wait until all the books are retrieved
        while(!requestHelper.isDone) { /*Loading*/ }
        return bookDetail
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = View.VISIBLE
    }

    override fun onPostExecute(result: Book?) {
        super.onPostExecute(result)
        progressBar.visibility = View.INVISIBLE

        // Start List by passing result list
        Log.d("DEBUGGG : ", parentActivity.javaClass.name)
        val intent = Intent(parentActivity, BookListActivity::class.java)
        intent!!.putParcelableArrayListExtra("BOOK_LIST", result as ArrayList<Book>)
        Log.d(this.javaClass.name,"OnPostExecute")

        parentActivity.startActivity(intent)
    }
}