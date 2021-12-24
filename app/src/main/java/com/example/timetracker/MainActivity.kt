package com.example.timetracker

import android.app.Activity
import android.content.Intent
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
    val LAUNCH_POPUP_ACTIVITY: Int = 1
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
//            db.addName("Testing", "Should work", R.drawable.ic_launcher)
//            dataSet.add(
//                DataModel(
//                10, "Testing", "Should work", R.drawable.ic_launcher
//            )
//            )
//            adapter!!.notifyDataSetChanged()

//            val intent = Intent(this, PopUpWindow::class.java)
//            intent.putExtra("popuptitle", "Add Category")
//            intent.putExtra("popuptext", "Not Implemented Yet")
//            intent.putExtra("popupbtn", "Create")
//            startActivityForResult(intent, LAUNCH_POPUP_ACTIVITY)

            val b = Bundle()
            b.putString("popuptitle", "Add Category At Popup")
            b.putString("popuptext", "Not Implemented Yet")
            b.putString("popupbtn", "Create")
            openPostPopupActivityCustom.launch(b)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == LAUNCH_POPUP_ACTIVITY) {
//            if (resultCode == Activity.RESULT_OK) {
//                val db = MyData(this, null)
//                val resultName = data?.getStringExtra("name") ?: "Default"
//                val resultDescription = data?.getStringExtra("description") ?: "Default"
//                val resultImage = data?.getIntExtra("image", R.drawable.ic_launcher) ?: R.drawable.ic_launcher
//                db.addName(resultName, resultDescription, resultImage)
//                dataSet.add(DataModel(10, resultName, resultDescription, resultImage))
//                adapter!!.notifyDataSetChanged()
//            }
//        }
//    }
}