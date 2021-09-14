package com.example.kanglibrary.view

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityBookListBinding
import com.example.kanglibrary.databinding.ActivitySplashBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import com.example.kanglibrary.view.adapter.BookAdapter
import com.example.kanglibrary.viewmodel.BookListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

/**
 * @file BookListActivity.kt
 * @date 04/09/2015
 * @brief Activity to display the list of Books through Recycler View
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookListBinding
    lateinit var resultList : ArrayList<Book>
    var liveBookList = MutableLiveData<List<Book>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name,"OnCreate")

        resultList = ArrayList<Book>()

        val query = "mongodb"
        var page = 1
        getAllBooks(query, page)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_list)
        binding.lifecycleOwner = this

        var viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)

        binding.rvBookList.layoutManager = LinearLayoutManager(this)

//        val observer : Observer<List<Book>> =
//            Observer { data ->
//                liveBookList.value = data
//        }
//
//        viewModel.liveData.observe(this, observer)
//        binding.rvBookList.adapter = BookAdapter(liveBookList)
    }
    fun getAllBooks(query : String, page : Int) {
        val retrofit = RetrofitClient.getInstance()
        val api = retrofit.create(RetrofitService::class.java)
        val call = api.getAllBooks(query, page)

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
                resultList.addAll(books)

                Log.d(this.javaClass.name, "getAllBooks > onResponse > List Count :  ${books.size} / ${page}")

                for(i in 0 until books.size) {
                    getBookDetail(books[i].isbn13!!, (page - 1) * 10 + i)

                }

                if(total / 10 > page) {
                    getAllBooks(query, page + 1)
                }
            }

            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBooks > onFailure > message : ${t.message}")

            }
        })
    }

    fun getBookDetail(isbn : String, index : Int){
        val retrofit = RetrofitClient.getInstance()
        val api = retrofit.create(RetrofitService::class.java)
        val call = api.getBookDetail(isbn)

        Log.d(this.javaClass.name,"getBookDetail")
        call.enqueue(object : Callback<Book> {
            override fun onResponse(
                call: Call<Book>,
                response: Response<Book>
            ) {
                Log.d(this.javaClass.name, "getBookDetail > onResponse >  ${response.body()}")
                val bookDetail = response.body() as Book

                // String to be displayed for author @ recycler view
                val authors = bookDetail.authors?.split(", ")
                if(authors!!.size > 1) {
                    bookDetail.authorsForItemLabel = "${authors[0]} and ${authors!!.size - 1} others"
                } else {
                    bookDetail.authorsForItemLabel = "${bookDetail.authors}"
                }

                resultList[index] = bookDetail

                updateRecyclerView()
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d(this.javaClass.name, "getBookDetail > onFailure > message : ${t.message}")

            }
        })
    }

    private fun updateRecyclerView() {
        liveBookList.value = resultList
        if(binding.rvBookList.adapter != null) {
            val adapter = binding.rvBookList.adapter
            //adapter?.notifyItemInserted(resultList.size - 1)
            adapter?.notifyDataSetChanged()
        } else {
            binding.rvBookList.adapter = BookAdapter(liveBookList)
        }
    }

}