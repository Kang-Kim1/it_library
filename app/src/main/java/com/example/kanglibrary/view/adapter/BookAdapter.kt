package com.example.kanglibrary.view.adapter

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ItemBookBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @file BookAdapter.kt
 * @date 09/09/2015
 * @brief Adapter class to hold Book data to be displayed through RecyclerView
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookAdapter(var bookList : LiveData<List<Book>>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    lateinit var progressBar : ProgressBar
    lateinit var bookDetail : Book

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemBookBinding>(inflater, R.layout.item_book, parent, false)
        progressBar = binding.pgDetailLoading
        progressBar.visibility = View.VISIBLE
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.value!!.size
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val book = bookList.value!![position]
        //getBookDetail(book)

        holder.bind(book)
    }

    inner class ViewHolder(private val binding : ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book : Book) {
            binding.book = book
            binding.executePendingBindings() // Prompt binding when any update is detected
        }
    }

    fun getBookDetail(book : Book) {
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
                progressBar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBookDetails > onFailure > message / isbn : ${t.message} / ${book.isbn13}")
                bookDetail = book
            }
        })
    }


}