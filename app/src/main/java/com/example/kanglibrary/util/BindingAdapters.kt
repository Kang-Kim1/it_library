package com.example.kanglibrary.util

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.kanglibrary.R
import kotlinx.android.synthetic.main.item_book.*

object BindingAdapters {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String) {
        Glide.with(imageView.context).load(url).error(R.drawable.img_splash_activity).into(imageView)
    }

}