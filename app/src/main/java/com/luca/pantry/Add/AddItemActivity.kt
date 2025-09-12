package com.luca.pantry.Add

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.luca.pantry.BaseActivity
import com.luca.pantry.EmptyActivity
import com.luca.pantry.R
import fragment.CameraFragment

class AddItemActivity : BaseActivity() {
    protected lateinit var add_manually_btn: MaterialButton
    protected lateinit var cancel_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_additem)

        setTextHeader("Aggiungi prodotto")

        // Carica subito la CameraView
        supportFragmentManager.beginTransaction()
            .replace(R.id.camera_fragment, CameraFragment())
            .commit()

        buttonAnimationConfig()
        setupCancelButton()
        setupAddmanuallyButton()
    }

    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig(){
        add_manually_btn = findViewById(R.id.btn_add_manually)
        cancel_btn = findViewById(R.id.btn_cancel)

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

    private fun setupCancelButton() {
        cancel_btn = findViewById(R.id.btn_cancel)

        cancel_btn.setOnClickListener {
            finish()
        }
    }

    private fun setupAddmanuallyButton() {
        add_manually_btn = findViewById(R.id.btn_add_manually)
        var addmanually = "addmanually"

        add_manually_btn.setOnClickListener {
            val intent = Intent(this, EmptyActivity::class.java).apply {
                putExtra("ORIGIN", addmanually)
            }
            startActivity(intent)
        }
    }
}

