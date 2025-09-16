package com.luca.pantry

import android.os.Bundle
import androidx.core.os.bundleOf
import com.luca.pantry.fragment.ItemFragment

class EmptyActivity : BaseActivity() {
    private lateinit var origin: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

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
                setTextHeader("Aggiungi prodotto")

                val itemFragment = ItemFragment()
                itemFragment.arguments = bundleOf("showBarcode" to true)

                supportFragmentManager.beginTransaction()
                    .replace(fragment, itemFragment)
                    .commit()
            }
        }
    }
}