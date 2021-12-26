package com.example.timetracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class CustomActivityContract(private val activityClass: Class<*>) : ActivityResultContract<Bundle, Bundle>() {
    override fun createIntent(context: Context, input: Bundle): Intent {
        return Intent(context, activityClass).apply {
            putExtras(input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bundle? {
        val data = intent?.extras
        return if (resultCode == Activity.RESULT_OK && data != null) data
        else null
    }
}