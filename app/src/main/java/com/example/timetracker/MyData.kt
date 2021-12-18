package com.example.timetracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//class MyData {
////    object Data {
////        val nameArray: ArrayList<String>
////            get() = arrayListOf("Yovel", "Idan")
////        val descriptionArray: ArrayList<String> = arrayListOf("Fullstack", "Data Engineer")
////        val drawableArray: ArrayList<Int> = arrayListOf(R.drawable.ic_launcher, R.drawable.ic_launcher)
////        val idArray: ArrayList<Int> = arrayListOf(0, 1)
////    }
//}
class MyData(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object{
        private val DATABASE_NAME = "TIME_TRACKER"
        private val DATABASE_VERSION = 1
        val CATEGORIES_TABLE_NAME = "categories"
        val ID_COL = "id"
        val NAME_COl = "name"
        val DESCRIPTION_COL = "description"
        val IMAGE_COL = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + CATEGORIES_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                DESCRIPTION_COL + " TEXT," +
                IMAGE_COL + " INTEGER " + ")")

        db.execSQL(query)
        addName(db, "Yovel", "Fullstack Developer", R.drawable.ic_launcher)
        addName(db, "Idan", "Data Engineer", R.drawable.ic_launcher)
        addName(db, "Yossi", "Teacher", R.drawable.ic_launcher)
        addName(db, "Avi", "Chef", R.drawable.ic_launcher)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME)
        onCreate(db)
    }

    fun addName(db: SQLiteDatabase, name: String, description: String, image: Int ){
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(DESCRIPTION_COL, description)
        values.put(IMAGE_COL, image)

        db.insert(CATEGORIES_TABLE_NAME, null, values)
    }

    fun getAllCategories(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + CATEGORIES_TABLE_NAME, null)
    }
}