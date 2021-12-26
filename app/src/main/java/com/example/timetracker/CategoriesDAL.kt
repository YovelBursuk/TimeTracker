package com.example.timetracker

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object CategoriesDAL {
    private val COLLECTION_NAME = "categories"
    private val CATEGORIES_ICONS_MAPPING = mapOf(
        "Default" to R.drawable.ic_launcher,
        "Plus" to R.drawable.ic_baseline_add_24
    )
    private val REVERSED_CATEGORIES_ICONS_MAPPING = CATEGORIES_ICONS_MAPPING.entries.associate { (k, v) -> v.toString() to k}

    fun addCategory(name: String, description: String, icon: Int, myPostCallback: MyPostCallback) {
        val db = FirebaseFirestore.getInstance()
        val category: MutableMap<String, Any> = HashMap()
        category["name"] = name
        category["description"] = description
        category["image"] = REVERSED_CATEGORIES_ICONS_MAPPING[icon.toString()] ?: "Default"

        db.collection(COLLECTION_NAME).add(category).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { myPostCallback.onPostCallback(it.id) }
            }
        }
    }

    fun getAllCategories(myGetCallback: MyGetCallback) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = ArrayList<DataModel>()
                for (doc in task.result!!) {
                    val elemImage = CATEGORIES_ICONS_MAPPING[doc.get("image")] ?: R.drawable.ic_launcher
                    val elem = DataModel(
                        doc.id,
                        doc.get("name") as String,
                        doc.get("description") as String,
                        elemImage
                    )
                    response.add(elem)
                }
                myGetCallback.onGetCallback(response)
            }
        }
    }
}