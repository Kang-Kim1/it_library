package com.example.kanglibrary.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class BookSearchResult (
        val error: String?,
        val total: String?,
        val page: String?,
        val books: List<Book>?
)

@Parcelize
data class Book(
        val error: String?,
        val title: String?,
        val subtitle: String?,
        val authors: String?,
        val publisher: String?,
        val language: String?,
        val isbn10: String?,
        val isbn13: String?,
        val pages: String?,
        val year: String?,
        val rating: String?,
        val desc: String?,
        val price: String?,
        val image: String?,
        val url: String?,
        val pdf: @RawValue PDF?
) : Parcelable

@Parcelize
data class PDF (
        @SerializedName("Chapter 1")
        val chapter1: String?,
        @SerializedName("Chapter 2")
        val chapter2: String?,
        @SerializedName("Chapter 3")
        val chapter3: String?,
        @SerializedName("Chapter 4")
        val chapter4: String?,
        @SerializedName("Chapter 5")
        val chapter5: String?,
        @SerializedName("Chapter 6")
        val chapter6: String?,
        @SerializedName("Chapter 7")
        val chapter7: String?,
        @SerializedName("Chapter 8")
        val chapter8: String?,
        @SerializedName("Chapter 9")
        val chapter9: String?,
        @SerializedName("Chapter 10")
        val chapter10: String?,
        @SerializedName("Chapter 11")
        val chapter11: String?,
        @SerializedName("Chapter 12")
        val chapter12: String?,
        @SerializedName("Chapter 13")
        val chapter13: String?
) : Parcelable