package com.example.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var dataSet: ArrayList<DataModel> = ArrayList()
    private var recycleView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: CustomAdapter? = null

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

//        for (i in 0 until MyData.Data.nameArray.size) {
//            dataSet.add(
//                DataModel(
//                MyData.Data.idArray[i],
//                MyData.Data.nameArray[i],
//                MyData.Data.descriptionArray[i],
//                MyData.Data.drawableArray[i]
//            )
//            )
//        }

        adapter = CustomAdapter(dataSet)
        recycleView?.adapter = adapter
    }
}