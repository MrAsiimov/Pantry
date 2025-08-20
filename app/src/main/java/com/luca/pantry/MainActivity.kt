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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.luca.pantry.R.id.menu_button

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
        val menu_btn: ImageButton = findViewById(menu_button)
        //Set shape to button
        menu_btn.background = shape
    }

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
    }

    //Set onClick method
    fun setMenuButton(){
        drawerLayout = findViewById(R.id.main)
        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> Toast.makeText(this, "Home premuto", Toast.LENGTH_SHORT).show()
                R.id.menu_container -> Toast.makeText(this, "Container premuto", Toast.LENGTH_SHORT).show()
                R.id.menu_items -> Toast.makeText(this, "Items premuto", Toast.LENGTH_SHORT).show()
                R.id.menu_expiring_product -> Toast.makeText(this, "Prodotti in scadenza premuto", Toast.LENGTH_SHORT).show()
                R.id.menu_settings -> Toast.makeText(this, "Impostazioni premuto", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        val menu_btn = findViewById<ImageButton>(menu_button)
        menu_btn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}