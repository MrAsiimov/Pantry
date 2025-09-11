package com.luca.pantry.Add

import android.os.Bundle
import com.luca.pantry.BaseActivity
import com.luca.pantry.R
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

    }

    override fun onResume() {
        super.onResume()

    }
}

