package com.example.timetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskActivity : AppCompatActivity() {
    private var dataSet: ArrayList<TaskDataModel> = ArrayList()
    private var recycleView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: CustomTasksAdapter? = null
    private var categoryId: String = ""
    private val openPostPopupActivityCustom =
        registerForActivityResult(CustomActivityContract(PopUpWindow::class.java)) { result ->
            if (result != null) {
                val resultName = result.getString("name") ?: "Default"
                val resultDescription = result.getString("description") ?: "Default"
                TasksDAL.addTask(categoryId, resultName, resultDescription, object: MyPostCallback {
                    override fun onPostCallback(value: String) {
                        dataSet.add(TaskDataModel(value, categoryId, resultName, resultDescription))
                        adapter!!.notifyDataSetChanged()
                    }
                })

            }
        }
    private val openTasksActivityCustom =
        registerForActivityResult(CustomActivityContract(TimeTaskActivity::class.java)) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        supportActionBar?.title = "TimeTracker - Tasks"
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        val categoryInfo = intent.extras
        val selectedCategoryId: String = categoryInfo?.get("categoryId") as String

        categoryId = selectedCategoryId
        recycleView = findViewById(R.id.my_tasks_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recycleView?.layoutManager = layoutManager
        recycleView?.itemAnimator = DefaultItemAnimator()

        adapter = CustomTasksAdapter(dataSet, object: MyIntentCallback {
            override fun onIntentCallback(bundle: Bundle) {
                openTasksActivityCustom.launch(bundle)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        recycleView?.adapter = adapter

        TasksDAL.getAllTasksByCategory(selectedCategoryId, object: MyTasksGetCallback {
            override fun onGetCallback(value: ArrayList<TaskDataModel>) {
                dataSet.addAll(value)
                adapter!!.notifyDataSetChanged()
            }
        })

        val myAddCategoryBtnView: FloatingActionButton = findViewById(R.id.add_task)
        myAddCategoryBtnView.setOnClickListener {
            val b = Bundle()
            b.putString("popuptitle", "Add Task")
            b.putString("popuptext", "Task Title:")
            b.putString("popupdescription", "Task Description:")
            b.putString("popupbtn", "Create")
            openPostPopupActivityCustom.launch(b)
        }
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
}