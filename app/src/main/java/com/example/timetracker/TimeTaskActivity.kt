package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimeTaskActivity : AppCompatActivity() {
    private var title = ""
    private var description = ""
    private var taskId = ""
    var timeTaskTitleView: TextView? = null
    var timeTaskDescriptionView: TextView? = null
    var startTaskButtonView: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_task)

        val bundle = intent.extras
        title = bundle?.getString("timeTaskTitle", "Title") ?: ""
        description = bundle?.getString("timeTaskDescription", "Description") ?: ""
        taskId = bundle?.getString("taskId", "Id") ?: ""

        timeTaskTitleView = findViewById(R.id.timeTaskTextView)
        timeTaskDescriptionView = findViewById(R.id.timeTaskDescriptionTextView)
        timeTaskTitleView?.text = title
        timeTaskDescriptionView?.text = description

        startTaskButtonView = findViewById(R.id.startTaskButton)
        startTaskButtonView?.setOnClickListener {
            TimeTaskDAL.startTask(taskId, object: MyPostCallback {
                override fun onPostCallback(value: String) {
                    val toast = Toast.makeText(applicationContext, "Started Task: $title", Toast.LENGTH_LONG)
                    toast.show()
                    disableButton()
                }
            })
        }

        TimeTaskDAL.getLastRunningTaskId(object: MyTimestampGetCallback {
            override fun onGetCallback(value: String) {
                if (value == taskId) {
                    disableButton()
                }
            }
        })
    }

    private fun disableButton() {
        startTaskButtonView?.text = getString(R.string.onRunningTaskButtonText)
        startTaskButtonView?.isEnabled = false
        startTaskButtonView?.isClickable = false
    }
}