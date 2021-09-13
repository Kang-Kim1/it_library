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
import com.example.kanglibrary.view.adapter.BookAdapter
import com.example.kanglibrary.viewmodel.BookListViewModel

/**
 * @file BookListActivity.kt
 * @date 04/09/2015
 * @brief Activity to display the list of Books through Recycler View
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookListBinding
    var liveBookList = MutableLiveData<List<Book>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name,"OnCreate")

//        val bundle = intent.extras
//        liveBookList.value = bundle!!.getString("BOOK_LIST") as ArrayList<Book>
        liveBookList.value = intent.getSerializableExtra("BOOK_LIST") as ArrayList<Book>

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_list)
        binding.lifecycleOwner = this

        var viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)

        binding.rvBookList.layoutManager = LinearLayoutManager(this)

        val observer : Observer<List<Book>> =
            Observer { data ->
                liveBookList.value = data

                binding.rvBookList.adapter = BookAdapter(liveBookList)
        }

        viewModel.liveData.observe(this, observer)
        binding.rvBookList.adapter = BookAdapter(liveBookList)
    }
    private fun setRecyclerView() {
        //binding.rvBookList

    }

    private fun updateList() {

    }


}