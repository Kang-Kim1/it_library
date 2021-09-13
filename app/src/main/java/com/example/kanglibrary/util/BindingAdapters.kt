package com.example.kanglibrary.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.kanglibrary.R

object BindingAdapters {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

}