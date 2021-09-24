package com.example.kanglibrary.viewmodel

import android.app.Application
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
    val liveBookData : MutableLiveData<ArrayList<Book>>
    val liveBooksCount : MutableLiveData<Int>
    val liveErrorTxt : MutableLiveData<String>
    val liveMemoData : MutableLiveData<Map<String,String>>

    private var bookList : ArrayList<Book>

    private val retrofit = RetrofitClient.getInstance()
    private val api = retrofit.create(RetrofitService::class.java)
    private var memoData : HashMap<String, String> = MemoManager.readMemo(getApplication<Application>().applicationContext)

    private lateinit var callAllList : Call<BookSearchResult>
    private lateinit var callBookDetail : Call<Book>

    init {
        Log.d(javaClass.name, "init begin")
        liveBookData =  MutableLiveData<ArrayList<Book>>()
        liveErrorTxt = MutableLiveData<String>()
        liveBooksCount = MutableLiveData<Int>()
        liveMemoData = MutableLiveData<Map<String,String>>()

        liveBooksCount.postValue(0)
        liveMemoData.postValue(memoData)

        bookList = ArrayList<Book>()
        getAllBooks("android", 1)
    }

    fun search(query : String) {
        memoData = MemoManager.readMemo(getApplication<Application>().applicationContext)
        callAllList.cancel()
        callBookDetail.cancel()
        while(!(callAllList.isCanceled && callBookDetail.isCanceled)) {Log.d("WAITING", "WAITING")}
        liveBookData.value?.clear()
        getAllBooks(query, 1)
    }

    fun addMemo(book : Book, memo : String) {
        Log.d(javaClass.name, "addMemo > ${memo} for ${book.isbn13}")
        MemoManager.writeMemo(book?.isbn13.toString(), memo, getApplication<Application>().applicationContext)
        memoData = MemoManager.readMemo(getApplication<Application>().applicationContext)
        liveMemoData.postValue(memoData)

        val index = book?.index as Int
        //while(index > bookList.size) { /* Wait until bookList is updated */ }
        bookList[index].memo = memo
        //liveBookData.postValue(bookList)
        liveBookData.setValue(bookList)
    }

    fun getAllBooks(query : String, page : Int) {
        callAllList = api.getAllBooks(query, page)
        Log.d(this.javaClass.name,"getAllBooks")
        callAllList.enqueue(object : Callback<BookSearchResult> {
            override fun onResponse(
                    call: Call<BookSearchResult>,
                    response: Response<BookSearchResult>
            ) {
                if(!callAllList.isCanceled) {
                    //Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response}")
                    //Log.d(this.javaClass.name, "getAllBooks > onResponse >  ${response.body()}")

                    val totalCount = response.body()?.total!!.toInt()
                    liveBooksCount.postValue(totalCount)

                    val books = response.body()?.books as ArrayList<Book>

                    if(totalCount == 0) {
                        Log.d("NO BOOKS FOUND", "NO BOOKS FOUND")
                        liveErrorTxt.postValue("No result found")
                        liveBookData.postValue(books)
                        return
                    }
                    // 111 resultList.addAll(books)
                    //liveBookData.value?.addAll(books)
                    bookList.addAll((page - 1) * 10, books)
                    liveBookData.setValue(bookList)

                    Log.d(this.javaClass.name, "getAllBooks > onResponse > List Count :  ${books.size} / ${page}")

                    for(i in 0 until books.size) {
                        getBookDetail(books[i].isbn13!!, (page - 1) * 10 + i)
                    }

                    if(totalCount / 10 > page) {
                        getAllBooks(query, page + 1)
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


    fun deleteMemo(book : Book) {
        MemoManager.deleteMemo(book?.isbn13.toString(), getApplication<Application>().applicationContext)
    }

    fun getBookDetail(isbn : String, index : Int){
//        val retrofit = RetrofitClient.getInstance()
//        val api = retrofit.create(RetrofitService::class.java)
//        val call = api.getBookDetail(isbn)
        callBookDetail = api.getBookDetail(isbn)

        Log.d(this.javaClass.name,"getBookDetail")
//        call.enqueue(object : Callback<Book> {
        callBookDetail.enqueue(object : Callback<Book>{
            override fun onResponse(
                    call: Call<Book>,
                    response: Response<Book>
            ) {
                if(!callBookDetail.isCanceled) {
                    //Log.d(this.javaClass.name, "getBookDetail > onResponse >  ${response.body()}")
                    val bookDetail = response.body() as Book
                    bookDetail.index = index

                    // String to be displayed for author @ recycler view
                    val authors = bookDetail.authors?.split(", ")
                    if (authors!!.size > 1) {
                        bookDetail.authorsForItemLabel = "${authors[0]} and ${authors!!.size - 1} others"
                    } else {
                        bookDetail.authorsForItemLabel = "${bookDetail.authors}"
                    }

                    // Add memo if exists
                    if(memoData.containsKey(bookDetail.isbn13)) {
                        bookDetail.memo = memoData[bookDetail.isbn13]
                    } else {
                        bookDetail.memo = ""
                    }

                    // 111 resultList[index] = bookDetail
                    //liveBookData.value!![index] = bookDetail
                    bookList[index] = bookDetail
                    liveBookData.postValue(bookList)
//                    liveBookData.value = bookList
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
}