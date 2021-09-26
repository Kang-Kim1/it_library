package com.example.kanglibrary.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import java.io.*

object MemoManager {
    private const val FILE_NAME = "memo_data.txt"

    private fun updateFile(json : String, context : Context) {
        // Write data into a file
        Log.d(javaClass.name, "updateFile > ${json.toString()}")
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }

    fun writeMemo(isbn13: String, memo: String, context: Context) {
        // Build Json Object
        var map : HashMap<String, String> = readMemo(context)
        map.put(isbn13, memo)
        var json = JSONObject(map as Map<String, String>).toString();

        updateFile(json, context)
    }

    fun deleteMemo(isbn13: String, context: Context) {
        var map : HashMap<String, String> = readMemo(context)
        map.remove(isbn13)
        var json = JSONObject(map as Map<String, String>).toString();

        updateFile(json, context)
    }

    fun readMemo(context: Context) : HashMap<String, String> {
        val file = File(context.filesDir, FILE_NAME)
        if(!file.exists()) {
            file.createNewFile()
            return HashMap<String,String>()
        } else {
            val bufferedReader: BufferedReader = File(context.filesDir, FILE_NAME).bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            if(inputString == "")
                return HashMap<String,String>()

            val jsonObject: JsonObject = JsonParser().parse(inputString).asJsonObject
            return Gson().fromJson<HashMap<String, String>>(jsonObject.toString(), HashMap::class.java)
        }
    }

}