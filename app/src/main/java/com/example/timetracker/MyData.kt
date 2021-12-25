package com.example.timetracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyData(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "TIME_TRACKER"
        private const val DATABASE_VERSION = 1
        const val CATEGORIES_TABLE_NAME = "categories"
        const val ID_COL = "id"
        const val NAME_COl = "name"
        const val DESCRIPTION_COL = "description"
        const val IMAGE_COL = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY = "CREATE TABLE $CATEGORIES_TABLE_NAME ($ID_COL INTEGER PRIMARY KEY, $NAME_COl TEXT, $DESCRIPTION_COL TEXT, $IMAGE_COL INTEGER)"
        val CREATE_VALUES_QUERY = "INSERT INTO $CATEGORIES_TABLE_NAME ($ID_COL, $NAME_COl, $DESCRIPTION_COL, $IMAGE_COL) " +
                "VALUES (1, 'Yovel', 'Fullstack Developer', ${R.drawable.ic_launcher})," +
                "(2, 'Idan', 'Data Engineer', ${R.drawable.ic_launcher})," +
                "(3, 'Yossi', 'Teacher', ${R.drawable.ic_launcher})"

        db.execSQL(CREATE_TABLE_QUERY)
        db.execSQL(CREATE_VALUES_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CATEGORIES_TABLE_NAME")
        onCreate(db)
    }

    fun addName(name: String, description: String, image: Int ){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(DESCRIPTION_COL, description)
        values.put(IMAGE_COL, image)

        db.insert(CATEGORIES_TABLE_NAME, null, values)
    }

    fun getAllCategories(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $CATEGORIES_TABLE_NAME", null)
    }
}