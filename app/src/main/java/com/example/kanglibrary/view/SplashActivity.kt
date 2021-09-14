package com.example.kanglibrary.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivitySplashBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.network.BookListRequestAsyncTask
import com.example.kanglibrary.network.RequestHelper
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    lateinit var results : List<Book>
    lateinit var binding : ActivitySplashBinding
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name,"OnCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        progressBar = binding.pbLoading
        progressBar.visibility = View.VISIBLE
        Log.d("APP STARTED", "JUST PASSED")
    }
    

    override fun onStart() {
        super.onStart()

        progressBar.visibility = View.INVISIBLE

        val intent = Intent(this, BookListActivity::class.java)
        startActivity(intent)
    }



    override fun onResume() {
        super.onResume()
    }


    /**
     * Returns mac address for a given index
     * @param : -
     * @return: Appliance's Mac address
     * @throws ArrayIndexOutOfBoundsException
     */
    private fun loadBookInfo(): Boolean {
        return true
    }
}