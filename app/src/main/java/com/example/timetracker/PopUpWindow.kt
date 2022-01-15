package com.example.timetracker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils

class PopUpWindow : AppCompatActivity() {
    private var popupTitle = ""
    private var popupText = ""
    private var popupButton = ""
    private var popupDescription = ""
    private var dropdownDataset: List<String> = CATEGORIES_ICONS_MAPPING.keys.toList()
    var popupWindowTitleView: TextView? = null
    var popupWindowTextView: TextView? = null
    var popupWindowDescriptionView: TextView? = null
    var popupWindowIconView: TextView? = null
    var popupWindowButtonView: Button? = null
    var popupWindowBackgroundView: ConstraintLayout? = null
    var popupWindowViewWithBorder: CardView? = null
    var inputTitleView: EditText? = null
    var inputDescriptionView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_window)

        val customDropdownAdapter = CustomDropdownAdapter(this, dropdownDataset)
        val dropdownView: Spinner = findViewById(R.id.popup_window_icon_dropdown)
        dropdownView.adapter = customDropdownAdapter

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popupWindowBackgroundView?.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        popupWindowViewWithBorder?.alpha = 0f
        popupWindowViewWithBorder?.animate()?.alpha(1f)?.setDuration(500)?.setInterpolator(
            DecelerateInterpolator()
        )?.start()

        val bundle = intent.extras
        popupTitle = bundle?.getString("popuptitle", "Title") ?: ""
        popupText = bundle?.getString("popuptext", "Text") ?: ""
        popupDescription = bundle?.getString("popupdescription", "Description") ?: ""
        val popupIcon = bundle?.getBoolean("popupicon", false)
        popupButton = bundle?.getString("popupbtn", "Button") ?: ""

        popupWindowTitleView = findViewById(R.id.popup_window_title)
        popupWindowTextView = findViewById(R.id.popup_window_text)
        popupWindowDescriptionView = findViewById(R.id.popup_window_description)
        popupWindowIconView = findViewById(R.id.popup_window_icon)
        popupWindowButtonView = findViewById(R.id.popup_window_button)
        popupWindowBackgroundView = findViewById(R.id.popup_window_background)
        popupWindowViewWithBorder = findViewById(R.id.popup_window_view_with_border)
        inputTitleView = findViewById(R.id.inputTitle)
        inputDescriptionView = findViewById(R.id.inputDescription)

        popupWindowTitleView?.text = popupTitle
        popupWindowTextView?.text = popupText
        popupWindowDescriptionView?.text = popupDescription
        popupWindowButtonView?.text = popupButton

        if (popupIcon == false) {
            popupWindowIconView?.visibility = View.GONE
            dropdownView.visibility = View.GONE
        }

        popupWindowButtonView?.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("name", inputTitleView?.text.toString())
            returnIntent.putExtra("description", inputDescriptionView?.text.toString())
            val selectedIcon: String = dropdownView.selectedItem as String
            returnIntent.putExtra("image", CATEGORIES_ICONS_MAPPING[selectedIcon])
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        popupWindowBackgroundView = findViewById(R.id.popup_window_background)
        popupWindowViewWithBorder = findViewById(R.id.popup_window_view_with_border)

        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popupWindowBackgroundView?.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        popupWindowViewWithBorder?.animate()?.alpha(0f)?.setDuration(500)?.setInterpolator(
            DecelerateInterpolator()
        )?.start()

        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}