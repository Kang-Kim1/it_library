package com.example.kanglibrary.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivitySplashBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.network.BookListRequestAsyncTask
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    lateinit var results : List<Book>
    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name,"OnCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Log.d("APP STARTED", "JUST PASSED")
//
//        loadingTV.text =  "Loading now ...."
//        loadingTV.setC
    }

    var result : List<Book> = ArrayList<Book>()
    var resultList = ArrayList<Book>()
    var isDone : Boolean = false;
    val retrofit = RetrofitClient.getInstance()
    val api = retrofit.create(RetrofitService::class.java)
    var bookCount : Int = 0

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



    override fun onStart() {
        super.onStart()

        getAllBooks("mongodb", 1)

//        val asyncTask = BookListRequestAsyncTask(binding.pbLoading, this)
//        results = asyncTask.execute().get()
        //this.results = asyncTask.getResultList()
        Log.d(this.javaClass.name,"onSTart done")
    }



    override fun onResume() {
        super.onResume()
    }


    /**
     * Returns mac address for a given index
     * @param : -
     * @return: Appliance's Mac address
     * @throws ArrayIndexOutOfBoundsException
     */
    private fun loadBookInfo(): Boolean {
        return true
    }
}