package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val categoryInfo = intent.extras
        Log.d("Message", "Yovel this is the category data $categoryInfo")
    }
}