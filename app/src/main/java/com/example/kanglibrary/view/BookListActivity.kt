package com.example.kanglibrary.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityBookListBinding
import com.example.kanglibrary.view.adapter.BookAdapter
import com.example.kanglibrary.viewmodel.BookListViewModel


/**
 * @file BookListActivity.kt
 * @date 16/09/2021
 * @brief Activity to display the list of Books through Recycler View
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBookListBinding
    private lateinit var progressBar : ProgressBar
    private lateinit var searchET : TextView
    private lateinit var viewModel : BookListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this.javaClass.name, "OnCreate")
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_list)
        binding.lifecycleOwner = this
        progressBar = binding.pgListLoading
        progressBar.bringToFront()
        binding.rvBookList.layoutManager = LinearLayoutManager(this)
        binding.btnSearch.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            searchBooks()
        })

        searchET = binding.etSearchKeyword
        searchET.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchBooks()
                return@OnKeyListener true
            }
            false
        })

        viewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
        // Observer for Books
        viewModel.liveBookData.observe(this, Observer {
            Log.d(javaClass.name, "Book data update observed ")
            if (binding.rvBookList.adapter != null) {
                binding.rvBookList.adapter?.notifyDataSetChanged()
            } else {
                //binding.rvBookList.adapter = BookAdapter(bookList)
                binding.rvBookList.adapter = BookAdapter(viewModel.liveBookData)
            }
            progressBar.visibility = View.GONE
        })
        // Observer for Error message
        viewModel.liveErrorTxt.observe(this, Observer {
            Log.d(javaClass.name, "Error msg observed ")
            Toast.makeText(this, viewModel.liveErrorTxt.value, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })
        // Observer for Error message
        viewModel.liveBooksCount.observe(this, Observer {
            Log.d(javaClass.name, "Count change observed : ${it}")
            binding.totalCount = it
        })
    }

    private fun searchBooks() {
        viewModel.search(searchET.text.toString())
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.name, "onResume() >>> ")
    }

    override fun onBackPressed() {
        /* Disable 'Back' Button for Activity only */
        val count = supportFragmentManager.backStackEntryCount
        if (count != 0) {
            supportFragmentManager.popBackStack()
        }
    }
}
