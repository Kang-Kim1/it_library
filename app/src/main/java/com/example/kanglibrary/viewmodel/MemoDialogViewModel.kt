package com.example.kanglibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kanglibrary.util.MemoManager

/**
 * @file MemoDialogViewModel.kt
 * @date 23/09/2021
 * @brief A ViewModel class for MemoDialog
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class MemoDialogViewModel(application : Application) : AndroidViewModel(application) {
    val liveMemoData = MutableLiveData<HashMap<String, String>>()

    init {

    }

    fun addMemo(isbn13 : String, memo : String) {
        MemoManager.writeMemo(isbn13, memo, getApplication<Application>().applicationContext)
    }

    fun deleteMemo(isbn13 : String) {
        MemoManager.deleteMemo(isbn13, getApplication<Application>().applicationContext)
    }
}