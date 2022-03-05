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
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityBookListBinding
import com.example.kanglibrary.view.adapter.BookAdapter
import com.example.kanglibrary.viewmodel.BookListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


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
    private var searchKeyword ="";

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
//            progressBar.visibility = View.VISIBLE
            turnOnLoading(true)
            searchKeyword = searchET.text.toString();
            searchBooks(true)
        })

        binding.rvBookList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // If reaches the end
                if(binding.rvBookList.size != viewModel.liveBooksCount.value) {
                    if (!recyclerView.canScrollVertically(1)) {
                        turnOnLoading(true)
                        searchBooks(false)
                    }
                }
            }
        })

        searchET = binding.etSearchKeyword
        searchET.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                turnOnLoading(true)
                searchKeyword = searchET.text.toString();
                searchBooks(true)
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
                binding.rvBookList.adapter = BookAdapter(viewModel.liveBookData)
            }
            turnOnLoading(false)
        })
        // Observer for Error message
        viewModel.liveErrorTxt.observe(this, Observer {
            Log.d(javaClass.name, "Error msg observed ")
            Toast.makeText(this, viewModel.liveErrorTxt.value, Toast.LENGTH_SHORT).show()
            turnOnLoading(false)
        })
        // Observer for Error message
        viewModel.liveBooksCount.observe(this, Observer {
            Log.d(javaClass.name, "Count change observed : ${it}")
            binding.totalCount = it
        })
    }

    private fun searchBooks(newSearch : Boolean) {
        viewModel.search(searchKeyword, newSearch)
    }

    private fun turnOnLoading(on : Boolean) {
        if(on) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        progressBar.visibility = if(on) View.VISIBLE else View.GONE
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
