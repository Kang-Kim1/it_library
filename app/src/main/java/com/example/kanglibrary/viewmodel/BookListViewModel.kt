package com.example.kanglibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kanglibrary.model.Book

/**
 * @file Appliance.kt
 * @date 14/09/2021
 * @brief A ViewModel class for BookListActivity
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListViewModel : ViewModel() {
    val liveListData : MutableLiveData<ArrayList<Book>> = MutableLiveData<ArrayList<Book>>()

    init {
        liveListData.value = ArrayList<Book>()
    }


}