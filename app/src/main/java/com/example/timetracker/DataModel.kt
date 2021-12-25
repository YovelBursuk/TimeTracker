package com.example.timetracker

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