package com.example.timetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        supportActionBar?.title = "TimeTracker - Start Task"

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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_dashboard -> {
                startActivity(Intent(this, DashboardActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun disableButton() {
        startTaskButtonView?.text = getString(R.string.onRunningTaskButtonText)
        startTaskButtonView?.isEnabled = false
        startTaskButtonView?.isClickable = false
    }
}