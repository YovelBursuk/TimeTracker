package com.example.timetracker

import com.google.firebase.firestore.FirebaseFirestore

object TasksDAL {
    private val COLLECTION_NAME = "tasks"

    fun addTask(categoryId: String, name: String, description: String, myPostCallback: MyPostCallback) {
        val db = FirebaseFirestore.getInstance()
        val task: MutableMap<String, Any> = HashMap()
        task["categoryId"] = categoryId
        task["name"] = name
        task["description"] = description

        db.collection(COLLECTION_NAME).add(task).addOnCompleteListener { dbTask ->
            if (dbTask.isSuccessful) {
                dbTask.result?.let { myPostCallback.onPostCallback(it.id) }
            }
        }
    }

    fun getAllTasksByCategory(selectedCategoryId: String, myGetCallback: MyTasksGetCallback) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .whereEqualTo("categoryId", selectedCategoryId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = ArrayList<TaskDataModel>()
                    for (doc in task.result!!) {
                        val elem = TaskDataModel(
                            doc.id,
                            doc.get("categoryId") as String,
                            doc.get("name") as String,
                            doc.get("description") as String
                        )
                        response.add(elem)
                    }
                    myGetCallback.onGetCallback(response)
                }
        }
    }

    fun getAllTasks(myGetCallback: MyTasksGetCallback) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = ArrayList<TaskDataModel>()
                    for (doc in task.result!!) {
                        val elem = TaskDataModel(
                            doc.id,
                            doc.get("categoryId") as String,
                            doc.get("name") as String,
                            doc.get("description") as String
                        )
                        response.add(elem)
                    }
                    myGetCallback.onGetCallback(response)
                }
            }
    }
}