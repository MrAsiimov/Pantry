package com.luca.pantry

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.luca.pantry.R.id.menu_button
import com.luca.pantry.View.ContainerViewActivity

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
            val navIntent: Intent? = when (item.itemId) {
                R.id.menu_home -> {
                    Intent(this, MainActivity::class.java)
                }
                R.id.menu_container -> {
                    Intent(this, ContainerViewActivity::class.java)
                }
                R.id.menu_items -> {
                    Toast.makeText(this, "Items premuto", Toast.LENGTH_SHORT).show()
                    null
                }
                R.id.menu_expiring_product -> {
                    Toast.makeText(this, "Prodotti in scadenza premuto", Toast.LENGTH_SHORT).show()
                    null
                }
                R.id.menu_settings -> {
                    Intent(this, SettingActivity::class.java)
                }
                else -> null
            }

            navIntent?.let { intentToLaunch ->
                val targetClass: Class<*>? = intentToLaunch
                    .component
                    ?.className
                    ?.let { name -> Class.forName(name) }

                if (this::class.java != targetClass) {
                    startActivity(intentToLaunch)
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val menu_btn = findViewById<ImageButton>(menu_button)
        menu_btn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig(){
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

    private fun buttonShapeConfig(){
        //Take shape
        val shape: Drawable? = getDrawable(resources, R.drawable.rounded_menu_button, getTheme())
        //Take ImageButton ID
        val menu_btn: ImageButton = findViewById(menu_button)
        //Set shape to button
        menu_btn.background = shape
    }

    fun setTextHeader(text: String){
        val text_header = findViewById<TextView>(R.id.text_header)

        when(text){
            "Home" -> text_header.setText(R.string.text_header_home)
            "Container" -> text_header.setText(R.string.text_header_container)
            "Prodotti" -> text_header.setText(R.string.text_header_items)
            "Prodotti in scadenza" -> text_header.setText(R.string.text_header_expiring_products)
            "Impostazioni" -> text_header.setText(R.string.text_header_settings)
            "Aggiungi prodotto" -> text_header.setText(R.string.text_header_add_item_activity)
            "Aggiungi contenitore" -> text_header.setText(R.string.text_header_add_container_activity)
            "Nuovo Prodotto" -> text_header.setText(R.string.text_header_new_item)
        }
    }
}