package com.example.kanglibrary.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivitySplashBinding
import com.example.kanglibrary.model.Book

class SplashActivity : AppCompatActivity() {
    lateinit var results : List<Book>
    lateinit var binding : ActivitySplashBinding
    lateinit var progressBar : ProgressBar
    lateinit var loadingTV : TextView
    var isLoading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        Log.d(this.javaClass.name, "OnCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        progressBar = binding.pbLoading
        loadingTV = binding.tvLoadingText
        progressBar.visibility = View.VISIBLE
        Log.d("APP STARTED", "JUST PASSED")
    }
    

    override fun onStart() {
        super.onStart()
        progressBar.visibility = View.INVISIBLE

        Handler().postDelayed({
        }, 5000)
        val intent = Intent(this, BookListActivity::class.java)
        startActivity(intent)
    }


    private fun showLoadingAnimation() {
        val loadingText : String = "Getting books now ..."
        val currTextBuilder = StringBuilder()
        Log.d("showLoadingAnimation()", "outside")
        for(i in 0 until loadingText.length) {
            Handler().postDelayed({
                currTextBuilder.append(loadingText[i])
                loadingTV.text = currTextBuilder
            }, 10000)
            print("plz")
        }
        isLoading = false
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