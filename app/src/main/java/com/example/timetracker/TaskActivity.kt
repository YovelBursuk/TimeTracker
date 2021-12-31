package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
                val resultImage = result.getInt("image", R.drawable.ic_launcher)
                TasksDAL.addTask(categoryId, resultName, resultDescription, resultImage, object: MyPostCallback {
                    override fun onPostCallback(value: String) {
                        dataSet.add(TaskDataModel(value, categoryId, resultName, resultDescription, resultImage))
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
            }
        })
        recycleView?.adapter = adapter

        TasksDAL.getAllTasks(selectedCategoryId, object: MyTasksGetCallback {
            override fun onGetCallback(value: ArrayList<TaskDataModel>) {
                dataSet.addAll(value)
                adapter!!.notifyDataSetChanged()
            }
        })
//
//
//
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
}