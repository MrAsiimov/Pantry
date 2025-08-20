package com.luca.pantry

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
//import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }*/

        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    }

    override fun onResume() {
        super.onResume()

        //Set button
        buttonShapeConfig()
        buttonAnimationConfig()
        setMenuButton()

    }


    //Set button shape
    fun buttonShapeConfig(){
        //Take shape
        val shape: Drawable? = getDrawable(resources, R.drawable.rounded_menu_button, getTheme())
        //Take ImageButton ID
        val menu_btn: ImageButton = findViewById(R.id.menu_button)
        //Set shape to button
        menu_btn.background = shape
    }

    fun buttonAnimationConfig(){
        val menu_btn = findViewById<ImageButton>(R.id.menu_button)

        menu_btn.setOnTouchListener { v, event ->
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

    //Set onClick method
    fun setMenuButton(){
        val menu_btn = findViewById<ImageButton>(R.id.menu_button)
        menu_btn.setOnClickListener {
            Toast.makeText(this, "Premuto il menu Button", Toast.LENGTH_SHORT).show()
        }
    }
}