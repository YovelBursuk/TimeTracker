package com.example.timetracker

import com.google.firebase.firestore.FirebaseFirestore

object CategoriesDAL {
    private val COLLECTION_NAME = "categories"

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
                val response = ArrayList<CategoryDataModel>()
                for (doc in task.result!!) {
                    val elemImage = CATEGORIES_ICONS_MAPPING[doc.get("image")] ?: R.drawable.ic_launcher
                    val elem = CategoryDataModel(
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