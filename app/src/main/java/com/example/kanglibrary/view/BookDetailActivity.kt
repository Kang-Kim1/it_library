package com.example.kanglibrary.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityDetailsBinding
import com.example.kanglibrary.model.Book

/**
 * @file Appliance.kt
 * @date 21/09/2021
 * @brief Activity class for book detail
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(javaClass.name, " INTENT HAS BEEN STARTED onCreate()")

        val layoutInflater = LayoutInflater.from(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        val bookDetail = intent.getSerializableExtra("SELECTED_BOOK") as Book?
        binding.book = bookDetail

        val inflater = LayoutInflater.from(this)
    }

    override fun onStart() {
        super.onStart()
    }

    fun onClickURL(v: View) {
        val url = v.tag as String

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}