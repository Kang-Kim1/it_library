package com.example.kanglibrary.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ItemBookBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.view.BookDetailActivity
import kotlinx.android.synthetic.main.item_book.view.*

/**
 * @file BookAdapter.kt
 * @date 09/09/2015
 * @brief Adapter class to hold Book data to be displayed through RecyclerView
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookAdapter(var bookList : LiveData<ArrayList<Book>>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
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
        if(book.pdf != null)
            Log.d("PARSE Possible ?", book.pdf.toString())

        holder.bind(book)
        progressBar.visibility = View.INVISIBLE
    }
    /*
        inner class for Item Holder
     */
    inner class ViewHolder(private val binding : ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookDetail : Book) {
            binding.book = bookDetail
            binding.book
            binding.executePendingBindings() // Prompt binding when any update is detected

            this.itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(it.context.applicationContext, BookDetailActivity::class.java)
                intent.putExtra("SELECTED_BOOK", bookDetail)
                Log.d(javaClass.name, "bind > Intent starts BookDetailActivity")
                it.context.startActivity(intent)
            })
        }


    }
}