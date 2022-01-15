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

val CATEGORIES_ICONS_MAPPING = mapOf(
    "Default" to R.drawable.ic_launcher,
    "Workout" to R.drawable.workout_foreground,
    "Work" to R.drawable.work_foreground,
    "Study" to R.drawable.study_foreground,
    "Food" to R.drawable.food_foreground,
    "Rest" to R.drawable.vacation_foreground
)
val REVERSED_CATEGORIES_ICONS_MAPPING = CATEGORIES_ICONS_MAPPING.entries.associate { (k, v) -> v.toString() to k}

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