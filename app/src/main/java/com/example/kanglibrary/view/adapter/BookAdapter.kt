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
import kotlinx.android.synthetic.main.item_book.view.*
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
    lateinit var binding : ItemBookBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<ItemBookBinding>(inflater, R.layout.item_book, parent, false)
        progressBar = binding.pgDetailLoading
        progressBar.visibility = View.VISIBLE
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.value!!.size
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val book = bookList.value!![position]
        holder.bind(book)

        progressBar.visibility = View.INVISIBLE
    }

    inner class ViewHolder(private val binding : ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book : Book) {
            binding.book = book
            binding.executePendingBindings() // Prompt binding when any update is detected
        }
    }


}