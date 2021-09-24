package com.example.kanglibrary.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.ActivityDetailsBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.viewmodel.BookListViewModel

/**
 * @file Appliance.kt
 * @date 21/09/2021
 * @brief Activity class for book detail
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    private lateinit var viewModel : BookListViewModel
    private lateinit var bookDetail : Book
    private var hasMemo : Boolean = false
    private lateinit var isbn13 : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(javaClass.name, " INTENT HAS BEEN STARTED onCreate()")
        val layoutInflater = LayoutInflater.from(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)

        bookDetail = intent.getSerializableExtra("SELECTED_BOOK") as Book

        // !! unavoidable for below conditional statement...
        binding.book = bookDetail
        isbn13 = bookDetail.isbn13.toString()

        setSupportActionBar(binding.tbBookDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.outline_arrow_circle_left_black_24dp)
        supportActionBar?.title = ""
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)
        viewModel.liveMemoData.observe(this, Observer {
            Log.d(javaClass.name, "onResume > liveMemoData updated observed ${it.toString()} / ${bookDetail.isbn13}")
            if(it.containsKey(bookDetail.isbn13)) {
                Log.d(javaClass.name, "onResume > ${bookDetail.isbn13} has been updated")
                bookDetail.memo = it[isbn13]
                updateHasMemo()
            }
        })
    }

    override fun onStart() {
        super.onStart()
    }

    fun onClickURL(v: View) {
        val url = v.tag as String
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        Log.d(javaClass.name, "onCreateOptionMenu >>>")

        if(bookDetail?.memo != "") {
            Log.d("MEMO DETECTED ", bookDetail?.memo.toString())
            hasMemo = true
        }

        if(hasMemo) {
//            menu?.getItem(0)?.setIcon(R.drawable.outline_sticky_note_2_black_24dp)
            menu?.getItem(0)?.setIcon(R.drawable.outline_sticky_note_2_black_24dp)
        } else {
//            menu?.getItem(0)?.setIcon(R.drawable.outline_note_add_black_24dp)
            menu?.getItem(0)?.setIcon(R.drawable.outline_note_add_black_24dp)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return super.onOptionsItemSelected(item)
            }
            R.id.action_memo ->{
                val bookData = Bundle()
                bookData.putSerializable("BOOK_DETAIL", binding.book)
                if(hasMemo) {
                    val dialog = MemoEditDialog()
                    dialog.arguments = bookData
                    dialog.show(supportFragmentManager, "MEMO_DIALOG")
                } else {
                    val dialog = MemoAddDialog()
                    dialog.arguments = bookData
                    dialog.show(supportFragmentManager, "MEMO_DIALOG")
                }
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    fun updateHasMemo() {
        // Recall onCreateOptionsMenu
        this.invalidateOptionsMenu()
    }

}