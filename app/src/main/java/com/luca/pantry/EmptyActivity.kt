package com.luca.pantry

import android.os.Bundle
import fragment.ItemFragment

class EmptyActivity : BaseActivity() {
    private lateinit var origin: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        origin = intent.getStringExtra("ORIGIN").toString()

        when (origin) {
            "addmanually" -> {
                setTextHeader("Aggiungi prodotto")
            }
        }
        loadFrameLayout()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun loadFrameLayout() {
        origin = intent.getStringExtra("ORIGIN").toString()
        val fragment = R.id.empty_fragment

        when (origin) {
            "addmanually" -> {
                supportFragmentManager.beginTransaction()
                    .replace(fragment, ItemFragment())
                    .commit()
            }
        }
    }
}