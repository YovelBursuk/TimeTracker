package com.example.timetracker

import com.google.firebase.firestore.FirebaseFirestore

object TasksDAL {
    private val COLLECTION_NAME = "tasks"
    private val CATEGORIES_ICONS_MAPPING = mapOf(
        "Default" to R.drawable.ic_launcher,
        "Plus" to R.drawable.ic_baseline_add_24
    )
    private val REVERSED_CATEGORIES_ICONS_MAPPING = CATEGORIES_ICONS_MAPPING.entries.associate { (k, v) -> v.toString() to k}

    fun addTask(categoryId: String, name: String, description: String, icon: Int, myPostCallback: MyPostCallback) {
        val db = FirebaseFirestore.getInstance()
        val task: MutableMap<String, Any> = HashMap()
        task["categoryId"] = categoryId
        task["name"] = name
        task["description"] = description
        task["image"] = REVERSED_CATEGORIES_ICONS_MAPPING[icon.toString()] ?: "Default"

        db.collection(COLLECTION_NAME).add(task).addOnCompleteListener { dbTask ->
            if (dbTask.isSuccessful) {
                dbTask.result?.let { myPostCallback.onPostCallback(it.id) }
            }
        }
    }

    fun getAllTasks(selectedCategoryId: String, myGetCallback: MyTasksGetCallback) {
        val db = FirebaseFirestore.getInstance()
        db.collection(TasksDAL.COLLECTION_NAME)
            .whereEqualTo("categoryId", selectedCategoryId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = ArrayList<TaskDataModel>()
                    for (doc in task.result!!) {
                        val elemImage = CATEGORIES_ICONS_MAPPING[doc.get("image")] ?: R.drawable.ic_launcher
                        val elem = TaskDataModel(
                            doc.id,
                            doc.get("categoryId") as String,
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