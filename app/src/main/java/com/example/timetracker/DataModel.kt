package com.example.timetracker

import android.os.Bundle

class DataModel(val id: String,
                val name: String = "Name",
                val description: String = "Description",
                val image: Int)

class TaskDataModel(
    val id: String,
    val categoryId: String,
    val name: String = "Name",
    val description: String = "Description",
    val image: Int
)

interface MyGetCallback {
    fun onGetCallback(value: ArrayList<DataModel>)
}

interface MyTasksGetCallback {
    fun onGetCallback(value: ArrayList<TaskDataModel>)
}

interface MyPostCallback {
    fun onPostCallback(value: String)
}

interface MyIntentCallback {
    fun onIntentCallback(bundle: Bundle)
}