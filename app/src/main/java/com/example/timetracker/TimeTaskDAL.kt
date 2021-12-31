package com.example.timetracker

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object TimeTaskDAL {
    private val COLLECTION_NAME = "time"

    fun startTask(taskId: String, myPostCallback: MyPostCallback) {
        val db = FirebaseFirestore.getInstance()
        val timeObject: MutableMap<String, Any> = HashMap()
        timeObject["startedAt"] = FieldValue.serverTimestamp()
        timeObject["taskId"] = taskId

        db.collection(COLLECTION_NAME).add(timeObject).addOnCompleteListener { dbTask ->
            if (dbTask.isSuccessful) {
                dbTask.result?.let { myPostCallback.onPostCallback(it.id) }
            }
        }
    }

    fun getLastRunningTaskId(myCallback: MyTimestampGetCallback) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .orderBy("startedAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val res = task.result
                    myCallback.onGetCallback(res?.first()?.get("taskId") as String)
                }
            }
    }
}