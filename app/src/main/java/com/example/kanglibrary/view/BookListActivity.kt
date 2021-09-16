package com.example.kanglibrary.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityBookListBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import com.example.kanglibrary.view.adapter.BookAdapter
import com.example.kanglibrary.viewmodel.BookListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @file BookListActivity.kt
 * @date 04/09/2015
 * @brief Activity to display the list of Books through Recycler View
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookListBinding
    lateinit var liveBookList : MutableLiveData<ArrayList<Book>>
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name,"OnCreate")

        val query = "mongodb"
        var page = 1
        getAllBooks(query, page)

        var viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)
        liveBookList = viewModel.liveListData

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_list)
        binding.lifecycleOwner = this

        progressBar = binding.pgListLoading
        progressBar.visibility = View.VISIBLE

        binding.rvBookList.layoutManager = LinearLayoutManager(this)

        // Set observer for LiveData  => REVIEW REQUIRED
        val listObserver = Observer<ArrayList<Book>> {
//            val adapter = binding.rvBookList.adapter
//            adapter?.notifyDataSetChanged()
//            Log.d("OBSERVER ", "DATA UPDATED !!!!!")
//            updateRecyclerView()

        }
        viewModel.liveListData.observe(this, listObserver)

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
                // 111 resultList.addAll(books)
                liveBookList.value?.addAll(books)

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
                // 111 resultList[index] = bookDetail
                liveBookList.value!![index] = bookDetail
                updateRecyclerView()
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d(this.javaClass.name, "getBookDetail > onFailure > message : ${t.message}")

            }
        })
    }


    override fun onBackPressed() {
        /* Disable 'Back' Button */
    }

    private fun updateRecyclerView() {
        if(binding.rvBookList.adapter != null) {
            val adapter = binding.rvBookList.adapter
            adapter?.notifyDataSetChanged()
        } else {
            binding.rvBookList.adapter = BookAdapter(liveBookList)
            progressBar.visibility = View.INVISIBLE
        }
    }
}