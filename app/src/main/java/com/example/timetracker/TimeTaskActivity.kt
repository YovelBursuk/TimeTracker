package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimeTaskActivity : AppCompatActivity() {
    private var title = ""
    private var taskId = ""
    var timeTaskTitleView: TextView? = null
    var startTaskButtonView: Button? = null

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_task)

        val bundle = intent.extras
        title = bundle?.getString("timeTaskTitle", "Title") ?: ""
        taskId = bundle?.getString("taskId", "Id") ?: ""

        timeTaskTitleView = findViewById(R.id.timeTaskTextView)
        var tempTitle = "$title \n $taskId"

        startTaskButtonView = findViewById(R.id.startTaskButton)
        startTaskButtonView?.setOnClickListener {
            TimeTaskDAL.startTask(taskId, object: MyPostCallback {
                override fun onPostCallback(value: String) {
                    val toast = Toast.makeText(applicationContext, "Start Task: $title", Toast.LENGTH_SHORT)
                    toast.show()
                    startTaskButtonView?.text = "Task is running!"
                    startTaskButtonView?.isEnabled = false
                    startTaskButtonView?.isClickable = false
                }
            })
        }

        TimeTaskDAL.getLastRunningTaskId(object: MyTimestampGetCallback {
            override fun onGetCallback(value: String) {
                tempTitle += "\n $value"
                timeTaskTitleView?.text = tempTitle

                if (value == taskId) {
                    startTaskButtonView?.text = "Task is running!"
                    startTaskButtonView?.isEnabled = false
                    startTaskButtonView?.isClickable = false
                }
            }
        })

        timeTaskTitleView?.text = tempTitle

    }
}