package com.luca.pantry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import com.luca.pantry.Add.AddContainerActivity
import com.luca.pantry.Add.AddItemActivity

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

        popupMenuCreate()

    }

    override fun onResume() {
        super.onResume()


    }

    //Create a pop-up menu for the add button (Add item/ add container)
    private fun popupMenuCreate(){
        val add_button = findViewById<ImageButton>(R.id.add_button_home)
        add_button.setOnClickListener {
            val popupMenu = PopupMenu(this, add_button)

            popupMenu.menuInflater.inflate(R.menu.popup_menu_add, popupMenu.menu)

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
                when (item.itemId) {
                    R.id.add_item -> {
                        val intent = Intent(this, AddItemActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.add_container -> {
                        val intent = Intent(this, AddContainerActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}