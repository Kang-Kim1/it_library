package com.example.kanglibrary.util.sqliteoperators

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(this.javaClass.toString(), "onCreate")
        var sql = "CREATE TABLE IF NOT EXISTS memos (_id PRIMARY KEY, isbn varchar(50) , memo text)"
        db?.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(this.javaClass.toString(), "onUpgrade")
        var sql = "DROP TABLE IF EXISTS memo"
        db?.execSQL(sql)
    }

    fun updateMemosTable(db: SQLiteDatabase?, isbn: String, memo: String) {
        Log.d(this.javaClass.toString(), "updateMemosTable")
        var sql = "INSERT OR REPLACE INTO memos (isbn, memo) VALUES ('${isbn}', '${memo}');"
        db?.execSQL(sql)
    }

    fun deleteMemo(db: SQLiteDatabase?, isbn: String) {
        Log.d(this.javaClass.toString(), "deleteMemo")
        var sql = "DELETE FROM memos WHERE isbn = '${isbn}';"
        db?.execSQL(sql)
    }

    fun loadMemos(): HashMap<String, String> {
        Log.d(this.javaClass.toString(), "loadMemos")
        var memoMap = HashMap<String, String>()
        val cursor = writableDatabase.rawQuery("SELECT * FROM memos", null)
        while (cursor.moveToNext()) {
            memoMap[cursor.getString(1)] = cursor.getString(2)
        }
        return memoMap
    }

}