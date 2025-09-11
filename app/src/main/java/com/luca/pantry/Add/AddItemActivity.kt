package com.luca.pantry.Add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import com.luca.pantry.BaseActivity
import com.luca.pantry.R
import com.luca.pantry.R.id.menu_button
import fragment.CameraFragment

class AddItemActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_additem)

        setTextHeader("Aggiungi prodotto")

        // Carica subito la CameraView
        supportFragmentManager.beginTransaction()
            .replace(R.id.camera_fragment, CameraFragment())
            .commit()

        buttonAnimationConfig()

    }

    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig(){
        val add_manually_btn = findViewById<Button>(R.id.btn_add_manually)
        val cancel_btn = findViewById<Button>(R.id.btn_cancel)

        add_manually_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }

        cancel_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }
    }
}

