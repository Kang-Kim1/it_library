package com.example.kanglibrary.viewmodel

import com.example.kanglibrary.util.sqliteoperators.DBHelper
import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kanglibrary.model.Book
import com.example.kanglibrary.model.BookSearchResult
import com.example.kanglibrary.network.RetrofitClient
import com.example.kanglibrary.network.RetrofitService
import com.example.kanglibrary.util.MemoManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @file Appliance.kt
 * @date 14/09/2021
 * @brief A ViewModel class for BookListActivity
 * @copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */
class BookListViewModel(application : Application) : AndroidViewModel(application) {
    val dbMode : Boolean = true

    lateinit var liveBookData : MutableLiveData<ArrayList<Book>>
    lateinit var liveBooksCount : MutableLiveData<Int>
    lateinit var liveErrorTxt : MutableLiveData<String>
    lateinit var liveMemoData : MutableLiveData<Map<String,String>>

    // Default search value
    private var keyword = "android"
    private var page = 1
    private var itemsPerPage = 10
    private var totalCount = 0

    private lateinit var bookList : ArrayList<Book>
    private lateinit var dbHelper: DBHelper

    private val retrofit = RetrofitClient.getInstance()
    private val api = retrofit.create(RetrofitService::class.java)

    private lateinit var memoData : HashMap<String, String>

    private lateinit var callAllList : Call<BookSearchResult>
    private lateinit var callBookDetail : Call<Book>

    private val context = getApplication<Application>().applicationContext

    init {
        if(!::bookList.isInitialized) {
            initViewModel()
        }
    }

    private fun initViewModel() {
        Log.d(javaClass.name, "init begin")
        liveBookData =  MutableLiveData<ArrayList<Book>>()
        liveErrorTxt = MutableLiveData<String>()
        liveBooksCount = MutableLiveData<Int>()
        liveMemoData = MutableLiveData<Map<String,String>>()

        liveBooksCount.postValue(0)

        bookList = ArrayList<Book>()
        getAllBooks(keyword, page)

        if(dbMode) {
            dbHelper = DBHelper(this.context, "ITLibrary", null, 1)
            dbHelper.onCreate(dbHelper.writableDatabase)
            dbHelper.updateMemosTable(dbHelper.writableDatabase, "isbn1111", "TEST_DATA")
            memoData = dbHelper.loadMemos()
        } else {
            memoData = MemoManager.readMemo(getApplication<Application>().applicationContext)
        }
        liveMemoData.postValue(memoData)
    }

    fun search(query : String, newSearch : Boolean) {
        Log.d("SEARCH KEYWORKD", "${keyword} / ${page}")
        this.keyword = if (query == "") keyword else query
//        if(dbMode) {
//            this.memoData = dbHelper.loadMemos()
//        } else {
//            this.memoData = MemoManager.readMemo(context)
//        }

        this.callAllList.cancel()
        this.callBookDetail.cancel()
        while(!(callAllList.isCanceled && callBookDetail.isCanceled)) {Log.d(javaClass.name, "Search() > Waiting ")}
        if(newSearch) {
            liveBookData.value?.clear()
            page = 0
        } else {
            if(page * itemsPerPage > totalCount) {
                liveErrorTxt.postValue("End of the list")
                return
            }
        }
        getAllBooks(keyword, ++page)
    }

    private fun getAllBooks(query : String, page : Int) {
        callAllList = api.getAllBooks(query, page)
        Log.d(this.javaClass.name,"getAllBooks")
        callAllList.enqueue(object : Callback<BookSearchResult> {
            override fun onResponse(
                    call: Call<BookSearchResult>,
                    response: Response<BookSearchResult>
            ) {
                if(!callAllList.isCanceled) {
                    totalCount = response.body()?.total?.toInt() as Int
                    liveBooksCount.postValue(totalCount)

                    val books = response.body()?.books as ArrayList<Book>
                    if(totalCount == 0) {
                        Log.d("NO BOOKS FOUND", "NO BOOKS FOUND")
                        liveErrorTxt.postValue("No result found")
                        liveBookData.postValue(books)
                        return
                    }
                    bookList.addAll((page - 1) * itemsPerPage, books)
                    liveBookData.value = bookList

                    Log.d(this.javaClass.name, "getAllBooks > onResponse > List Count :  ${books.size} / ${page}")
                    for(i in 0 until books.size) {
                        getBookDetail(books[i].isbn13.toString(), (page - 1) * 10 + i)
                    }
                }
            }

            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                Log.d(this.javaClass.name, "getAllBooks > onFailure > message : ${t.message}")
                if(t.message != t.message) {
                    liveErrorTxt.postValue("Request Failed - All Book List")
                }
            }
        })
    }

    fun getBookDetail(isbn : String, index : Int){
        callBookDetail = api.getBookDetail(isbn)

        Log.d(this.javaClass.name,"getBookDetail")
        callBookDetail.enqueue(object : Callback<Book>{
            override fun onResponse(
                    call: Call<Book>,
                    response: Response<Book>
            ) {
                if(!callBookDetail.isCanceled) {
                    val bookDetail = response.body() as Book
                    bookDetail.index = index

                    // String to be displayed for author @ recycler view
                    val authors = bookDetail.authors?.split(", ")
                    if (authors?.size as Int > 1) {
                        bookDetail.authorsForItemLabel = "${authors[0]} and ${authors.size - 1} others"
                    } else {
                        bookDetail.authorsForItemLabel = "${bookDetail.authors}"
                    }

                    // Add memo if exists
                    if(memoData.containsKey(bookDetail.isbn13)) {
                        bookDetail.memo = memoData[bookDetail.isbn13]
                    } else {
                        bookDetail.memo = ""
                    }

                    // When new search begins & previous search is not done
                    if(index > bookList.size) return

                    bookList[index] = bookDetail
                    liveBookData.postValue(bookList)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d(this.javaClass.name, "getBookDetail > onFailure > message : ${t.message}")
                if(t.message != "Canceled") {
                    liveErrorTxt.postValue("Request Failed - Book Detail for ${isbn}")
                }
            }
        })
    }

    private fun updateLiveMemoData(book : Book, memo : String) {
        if(dbMode) {
            memoData = dbHelper.loadMemos()
        } else {
            memoData = MemoManager.readMemo(context)
        }
        liveMemoData.postValue(memoData)

        val index = book?.index as Int
        bookList[index].memo = memo
        liveBookData.value = bookList
    }

    fun addMemoFileIO(book : Book, memo : String) {
        Log.d(javaClass.name, "addMemo > ${memo} for ${book.isbn13}")
        MemoManager.writeMemo(book?.isbn13.toString(), memo, context)
        updateLiveMemoData(book, memo)
    }

    fun addMemo(book : Book, memo : String) {
        Log.d(javaClass.name, "addMemo > ${memo} for ${book.isbn13}")
//        MemoManager.writeMemo(book?.isbn13.toString(), memo, context)
        dbHelper.updateMemosTable(dbHelper.writableDatabase, book?.isbn13.toString(), memo)
        updateLiveMemoData(book, memo)
    }

    fun deleteMemoFileIO(book : Book) {
        Log.d(javaClass.name, "delete > ${book.isbn13}")
        MemoManager.deleteMemo(book?.isbn13.toString(), context)
        updateLiveMemoData(book, "")
    }

    fun deleteMemo(book : Book) {
        Log.d(javaClass.name, "delete > ${book.isbn13}")
        dbHelper.deleteMemo(dbHelper.writableDatabase, book?.isbn13.toString())
        updateLiveMemoData(book, "")
    }

    fun editMemoFileIO(book : Book, memo : String) {
        Log.d(javaClass.name, "editMemo > ${memo} for ${book.isbn13}")
        MemoManager.writeMemo(book?.isbn13.toString(), memo, context)
        updateLiveMemoData(book, memo)
    }

}

