package com.example.kanglibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kanglibrary.model.Book

/**
 * @file Appliance.kt
 * @date 04/09/2015
 * @brief A class containing a single appliance's data
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListViewModel : ViewModel() {
    var liveData : MutableLiveData<ArrayList<Book>> = MutableLiveData<ArrayList<Book>>()

}