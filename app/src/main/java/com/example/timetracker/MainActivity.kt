package com.example.timetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var categoryDataSet: ArrayList<CategoryDataModel> = ArrayList()
    private var recycleView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: CustomAdapter? = null
    private val openPostPopupActivityCustom =
        registerForActivityResult(CustomActivityContract(PopUpWindow::class.java)) { result ->
            if (result != null) {
                val resultName = result.getString("name") ?: "Default"
                val resultDescription = result.getString("description") ?: "Default"
                val resultImage = result.getInt("image", R.drawable.ic_launcher)
                CategoriesDAL.addCategory(resultName, resultDescription, resultImage, object: MyPostCallback {
                    override fun onPostCallback(value: String) {
                        categoryDataSet.add(CategoryDataModel(value, resultName, resultDescription, resultImage))
                        adapter!!.notifyDataSetChanged()
                    }
                })

            }
        }
    private val openTasksActivityCustom =
        registerForActivityResult(CustomActivityContract(TaskActivity::class.java)) {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "TimeTracker - Categories"

        recycleView = findViewById(R.id.my_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recycleView?.layoutManager = layoutManager
        recycleView?.itemAnimator = DefaultItemAnimator()

        adapter = CustomAdapter(categoryDataSet, object: MyIntentCallback {
            override fun onIntentCallback(bundle: Bundle) {
                openTasksActivityCustom.launch(bundle)
            }
        })
        recycleView?.adapter = adapter

        CategoriesDAL.getAllCategories(object: MyGetCallback {
            override fun onGetCallback(value: ArrayList<CategoryDataModel>) {
                categoryDataSet.addAll(value)
                adapter!!.notifyDataSetChanged()
            }
        })



        val myAddCategoryBtnView: FloatingActionButton = findViewById(R.id.add_category)
        myAddCategoryBtnView.setOnClickListener {
            val b = Bundle()
            b.putString("popuptitle", "Add Category")
            b.putString("popuptext", "Category Title:")
            b.putString("popupdescription", "Category Description:")
            b.putBoolean("popupicon", true)
            b.putString("popupbtn", "Create")
            openPostPopupActivityCustom.launch(b)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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