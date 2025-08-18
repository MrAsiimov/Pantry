package com.luca.pantry

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luca.pantry.R.id.imageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val shape: Drawable? = getDrawable(resources, R.drawable.rounded_menu_button, getTheme())
        val menu_btn: ImageButton = findViewById(imageButton)
        menu_btn.background = shape

    }
}