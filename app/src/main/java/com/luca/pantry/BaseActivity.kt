package com.luca.pantry

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.luca.pantry.R.id.menu_button

abstract class BaseActivity: AppCompatActivity() {
    protected lateinit var navView: NavigationView
    protected lateinit var drawerLayout: DrawerLayout

    override fun setContentView(layoutResId: Int) {
        super.setContentView(R.layout.activity_base)

        layoutInflater.inflate(layoutResId, findViewById(R.id.content_frame), true)

        drawerLayout = findViewById(R.id.activity_base)
        navView = findViewById(R.id.nav_view)

        setupDrawer(navView)
        buttonShapeConfig()
        buttonAnimationConfig()
    }

    private fun setupDrawer(navView: NavigationView) {
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

    fun buttonShapeConfig(){
        //Take shape
        val shape: Drawable? = getDrawable(resources, R.drawable.rounded_menu_button, getTheme())
        //Take ImageButton ID
        val menu_btn: ImageButton = findViewById(menu_button)
        //Set shape to button
        menu_btn.background = shape
    }
}