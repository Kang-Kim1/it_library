package com.example.kanglibrary.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kanglibrary.R
import com.example.kanglibrary.databinding.FragmentDetailsBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.viewmodel.BookListViewModel

class BookDetailFragment : Fragment() {
    private lateinit var binding : FragmentDetailsBinding
    private lateinit var viewModel : BookListViewModel
    private lateinit var bookDetail : Book
    private lateinit var isbn13 : String

    private var hasMemo : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bookDetail = arguments?.getSerializable("SELECTED_BOOK") as Book as Book

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.ivThumbnailDetail.setOnClickListener(View.OnClickListener {
            val url = it.tag as String
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        })
        binding.book = bookDetail
        isbn13 = bookDetail.isbn13.toString()

        viewModel = ViewModelProvider(requireActivity()).get(BookListViewModel::class.java)

        (activity as AppCompatActivity).setSupportActionBar(binding.tbBookDetail)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.outline_arrow_circle_left_black_24dp)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        setHasOptionsMenu(true)

        updateHasMemo()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveMemoData.observe(this, Observer {
            Log.d(javaClass.name, "onResume > liveMemoData updated observed ${it.toString()} / ${bookDetail.isbn13}")
            if(it.containsKey(bookDetail.isbn13)) {
                Log.d(javaClass.name, "onResume > ${bookDetail.isbn13} has been updated")
                bookDetail.memo = it[isbn13]
            } else {
                bookDetail.memo = ""
            }
            updateHasMemo()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater : MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as AppCompatActivity).menuInflater.inflate(R.menu.menu_toolbar, menu)
        Log.d(javaClass.name, "onCreateOptionMenu >>>")
        hasMemo = false
        if(bookDetail?.memo != "") {
            Log.d("MEMO DETECTED ", "${bookDetail.title} has memo : ${bookDetail?.memo.toString()}")
            hasMemo = true
        }
        if(hasMemo) {
            menu?.getItem(0)?.setIcon(R.drawable.outline_sticky_note_2_black_24dp)
        } else {
            menu?.getItem(0)?.setIcon(R.drawable.outline_note_add_black_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(javaClass.name, "onOptionsItemSelected >>> ")
        when (item?.itemId) {
            android.R.id.home -> {
                onClose()
                return super.onOptionsItemSelected(item)
            }
            R.id.action_memo ->{
                val bookData = Bundle()
                bookData.putSerializable("BOOK_DETAIL", binding.book)
                if(hasMemo) {
                    val dialog = MemoEditDialog()
                    dialog.arguments = bookData
                    dialog.show((activity as AppCompatActivity).supportFragmentManager, "MEMO_DIALOG")
                } else {
                    val dialog = MemoAddDialog()
                    dialog.arguments = bookData
                    dialog.show((activity as AppCompatActivity).supportFragmentManager, "MEMO_DIALOG")
                }
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateHasMemo() {
        activity?.invalidateOptionsMenu()
    }

    private fun onClose() {
        Log.d(javaClass.name, "onClose()")
        activity?.onBackPressed()
    }

}