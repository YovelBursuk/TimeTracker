package com.example.timetracker

import android.os.Bundle

class DataModel(val id: String,
                val name: String = "Name",
                val description: String = "Description",
                val image: Int)

interface MyGetCallback {
    fun onGetCallback(value: ArrayList<DataModel>)
}

interface MyPostCallback {
    fun onPostCallback(value: String)
}

interface MyIntentCallback {
    fun onIntentCallback(bundle: Bundle)
}