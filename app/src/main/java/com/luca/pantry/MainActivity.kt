package com.luca.pantry

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.luca.pantry.R.id.menu_button

class MainActivity : BaseActivity() {
    private lateinit var themeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Load the setting about theme
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val mode = if (prefs.getBoolean("dark_mode", false))
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)

        setContentView(R.layout.activity_main)

        val txview = findViewById<TextView>(R.id.textView)
        txview.text = "banana"

    }

    override fun onResume() {
        super.onResume()

        //Set button menu
        //buttonShapeConfig()
        //buttonAnimationConfig()
        //setMenuButton()

    }


    /*//Set button shape
    fun buttonShapeConfig(){
        //Take shape
        val shape: Drawable? = getDrawable(resources, R.drawable.rounded_menu_button, getTheme())
        //Take ImageButton ID
        val menu_btn: ImageButton = findViewById(menu_button)
        //Set shape to button
        menu_btn.background = shape
    }

    //Set animation to menu button
    fun buttonAnimationConfig(){
        val menu_btn = findViewById<ImageButton>(menu_button)

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
    }*/



    /*
    fun darkmode(){
        themeSwitch = findViewById(R.id.dark_mode)
        // Carica lo stato salvato
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDark

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Cambia tema
            val mode = if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)

            // Salva preferenza
            prefs.edit()
                .putBoolean("dark_mode", isChecked)
                .apply()
        }
    }*/
}