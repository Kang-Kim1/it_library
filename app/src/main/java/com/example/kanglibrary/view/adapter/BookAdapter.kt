package com.example.kanglibrary.view.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ItemBookBinding
import com.example.kanglibrary.model.Book

/**
 * @file BookAdapter.kt
 * @date 09/09/2015
 * @brief Adapter class to hold Book data to be displayed through RecyclerView
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookAdapter(var bookList : LiveData<List<Book>>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemBookBinding>(inflater, R.layout.item_book, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.value!!.size
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val movie = bookList.value!![position]
        holder.bind(movie)

    }

    inner class ViewHolder(private val binding : ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book : Book) {
            binding.book = book
            binding.executePendingBindings() // Prompt binding when any update is detected
        }
    }


}