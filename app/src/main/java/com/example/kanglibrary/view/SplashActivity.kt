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

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var progressBar : ProgressBar
    private lateinit var loadingTV : TextView

    private val loadingText : String = "Getting books now..."

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
        showLoadingAnimation()
    }

    private fun showLoadingAnimation() {
        val textBuilder  = StringBuilder()
        Log.d("showLoadingAnimation()", "outside")

        recurAppendText(0, loadingText.length, textBuilder)

    }
    private fun recurAppendText(ptr : Int, size : Int, txt : StringBuilder) {

        if(ptr < size) {
            txt.append(loadingText[ptr])
            loadingTV.text = txt.toString()
            Handler().postDelayed({
                recurAppendText(ptr + 1, size, txt)
            }, 150)
        } else {
            progressBar.visibility = View.INVISIBLE
            val intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
        }
    }
}