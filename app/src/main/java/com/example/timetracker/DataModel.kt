package com.example.timetracker

import android.os.Bundle
import com.google.firebase.Timestamp

class DataModel(val id: String,
                val name: String = "Name",
                val description: String = "Description",
                val image: Int)

class TaskDataModel(
    val id: String,
    val categoryId: String,
    val name: String = "Name",
    val description: String = "Description"
)

class TimeEventDataModel(
    val id: String,
    val taskId: String,
    val startedAt: Timestamp
)

interface MyGetCallback {
    fun onGetCallback(value: ArrayList<DataModel>)
}

interface MyTasksGetCallback {
    fun onGetCallback(value: ArrayList<TaskDataModel>)
}

interface MyTimeEventsGetCallback {
    fun onGetCallback(value: ArrayList<TimeEventDataModel>)
}

interface MyTimestampGetCallback {
    fun onGetCallback(value: String)
}

interface MyPostCallback {
    fun onPostCallback(value: String)
}

interface MyIntentCallback {
    fun onIntentCallback(bundle: Bundle)
}

interface  MyFunctionCallback {
    fun onCallCallback(value: MutableMap<String, Float?>)
}