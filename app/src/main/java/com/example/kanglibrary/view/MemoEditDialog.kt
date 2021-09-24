package com.example.kanglibrary.view

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.kanglibrary.databinding.FragmentMemoEditBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.viewmodel.BookListViewModel
import com.example.kanglibrary.viewmodel.MemoDialogViewModel

/**
 * @file MemoFragment.kt
 * @date 23/09/2021
 * @brief A Dialog Fragment to add memo for selected book
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class MemoEditDialog : DialogFragment() {
    private lateinit var binding : FragmentMemoEditBinding
    private lateinit var book : Book
    private lateinit var viewModel : MemoDialogViewModel
//
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoEditBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this).get(MemoDialogViewModel::class.java)

        book = arguments?.getSerializable("BOOK_DETAIL") as Book

        // Deactivate background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.book = book

        binding.btnEditMemo.setOnClickListener(View.OnClickListener {
            val input = binding.etMemo.text.toString()
            if(input.length > 0) {
                viewModel.addMemo(book.isbn13.toString(), input)
                book.memo = input
                Toast.makeText(context, "Memo has been added", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Please write something..", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnDeleteMemo.setOnClickListener(View.OnClickListener {
            viewModel.deleteMemo(book.isbn13.toString())
            Toast.makeText(context, "Memo has been deleted", Toast.LENGTH_SHORT).show()
            dismiss()

        })

        binding.btnCancelMemo.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        return binding.root
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
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}