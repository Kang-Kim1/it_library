package com.example.kanglibrary.view

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kanglibrary.databinding.FragmentMemoAddBinding
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.viewmodel.BookListViewModel
import com.example.kanglibrary.viewmodel.MemoDialogViewModel

/**
 * @file MemoFragment.kt
 * @date 23/09/2021
 * @brief A Dialog Fragment to add memo for selected book
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class MemoAddDialog : DialogFragment() {
    private lateinit var binding : FragmentMemoAddBinding
    private lateinit var book : Book
    private lateinit var viewModel : BookListViewModel

    //
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoAddBinding.inflate(inflater, container, false)

        book = arguments?.getSerializable("BOOK_DETAIL") as Book

        // Deactivate background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnAddMemo.setOnClickListener(View.OnClickListener {
            val input = binding.etMemo.text.toString()
            if(input.length > 0) {
                viewModel.addMemo(book, input)
                Toast.makeText(context, "Memo has been added", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Please write something..", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnCancelMemo.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(BookListViewModel::class.java)
        viewModel.liveBookData.observe(this, Observer {
            val index = book.index as Int
            if(index < it.size) {
                Log.d(javaClass.name, "onActivityCreated >>> ${it[book.index!!].title}")
            }
        })
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
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}