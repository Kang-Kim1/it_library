package com.example.kanglibrary.view

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kanglibrary.databinding.FragmentMemoEditBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.viewmodel.BookListViewModel

/**
 * @file MemoFragment.kt
 * @date 23/09/2021
 * @brief A Dialog Fragment to add memo for selected book
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class MemoEditDialog : DialogFragment() {
    private lateinit var binding : FragmentMemoEditBinding
    private lateinit var book : Book
    private lateinit var viewModel : BookListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoEditBinding.inflate(inflater, container, false)
        book = arguments?.getSerializable("BOOK_DETAIL") as Book

        // Deactivate background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.book = book

        binding.btnEditMemo.setOnClickListener(View.OnClickListener {
            val input = binding.etMemo.text.toString()
            if(input.length > 0) {
                viewModel.editMemo(book, input)
                book.memo = input
                Toast.makeText(context, "Memo has been added", Toast.LENGTH_SHORT).show()
                onClose()
            } else {
                Toast.makeText(context, "Please write something..", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnDeleteMemo.setOnClickListener(View.OnClickListener {
            viewModel.deleteMemo(book)
            Toast.makeText(context, "Memo has been deleted", Toast.LENGTH_SHORT).show()
            onClose()
        })

        binding.btnCancelMemo.setOnClickListener(View.OnClickListener {
            onClose()
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BookListViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        // Set dialog size
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun onClose() {
        dismiss()
    }

}