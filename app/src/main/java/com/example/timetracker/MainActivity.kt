package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var dataSet: ArrayList<DataModel> = ArrayList()
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
                        dataSet.add(DataModel(value, resultName, resultDescription, resultImage))
                        adapter!!.notifyDataSetChanged()
                    }
                })

            }
        }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleView = findViewById(R.id.my_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recycleView?.layoutManager = layoutManager
        recycleView?.itemAnimator = DefaultItemAnimator()

        adapter = CustomAdapter(dataSet)
        recycleView?.adapter = adapter

        CategoriesDAL.getAllCategories(object: MyGetCallback {
            override fun onGetCallback(value: ArrayList<DataModel>) {
                dataSet.addAll(value)
                adapter!!.notifyDataSetChanged()
            }
        })



        val myAddCategoryBtnView: FloatingActionButton = findViewById(R.id.add_category)
        myAddCategoryBtnView.setOnClickListener {
            val b = Bundle()
            b.putString("popuptitle", "Add Category At Popup")
            b.putString("popuptext", "Not Implemented Yet")
            b.putString("popupbtn", "Create")
            openPostPopupActivityCustom.launch(b)
        }
    }
}