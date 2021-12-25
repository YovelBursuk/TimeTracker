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
        registerForActivityResult(PostPopupActivityContract()) { result ->
            if (result != null) {
                val db = MyData(this, null)
                val resultName = result.getString("name") ?: "Default"
                val resultDescription = result.getString("description") ?: "Default"
                val resultImage = result.getInt("image", R.drawable.ic_launcher)
                db.addName(resultName, resultDescription, resultImage)
                dataSet.add(DataModel(10, resultName, resultDescription, resultImage))
                adapter!!.notifyDataSetChanged()
            }
        }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleView = findViewById(R.id.my_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recycleView?.layoutManager = layoutManager
        recycleView?.itemAnimator = DefaultItemAnimator()

        val db = MyData(this, null)
        val categoriesCursor = db.getAllCategories()

        while (categoriesCursor!!.moveToNext()) {
            dataSet.add(DataModel(
                categoriesCursor.getInt(categoriesCursor.getColumnIndex(MyData.ID_COL)),
                categoriesCursor.getString(categoriesCursor.getColumnIndex(MyData.NAME_COl)),
                categoriesCursor.getString(categoriesCursor.getColumnIndex(MyData.DESCRIPTION_COL)),
                categoriesCursor.getInt(categoriesCursor.getColumnIndex(MyData.IMAGE_COL))
            ))
        }

        adapter = CustomAdapter(dataSet)
        recycleView?.adapter = adapter

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