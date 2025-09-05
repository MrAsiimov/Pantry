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
import android.view.animation.Animation
import android.util.Log
import android.view.MotionEvent
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.luca.pantry.R.id.menu_button

class MainActivity : BaseActivity() {
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
        setTextHeader("Home")

        //Create a pop-up menu for the add button (Add item/ add container)
        val add_button = findViewById<ImageButton>(R.id.add_button_home)
        add_button.setOnClickListener {
            val popupMenu = PopupMenu(this, add_button)

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            //Force icon on menu (Use try to prevent error)
            try {
                val fields = popupMenu.javaClass.getDeclaredField("mPopup")
                fields.isAccessible = true
                val menuPopupHelper = fields.get(popupMenu)
                val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                val setForceIcons = classPopupHelper.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                setForceIcons.invoke(menuPopupHelper, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popupMenu.setOnMenuItemClickListener { item ->
                Toast.makeText(this, "You Clicked " + item.title, Toast.LENGTH_SHORT).show()
                true
            }
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()


    }
}