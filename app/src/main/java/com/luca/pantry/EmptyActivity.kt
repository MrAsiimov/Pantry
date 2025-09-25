package com.luca.pantry

import android.os.Bundle
import androidx.core.os.bundleOf
import com.luca.pantry.fragment.ContainerFragment
import com.luca.pantry.fragment.ContainerViewFragment
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
                setTextHeader("Nuovo Prodotto")

                val itemFragment = ItemFragment()
                itemFragment.arguments = bundleOf("SHOWBARCODE" to true)

                supportFragmentManager.beginTransaction()
                    .replace(fragment, itemFragment)
                    .commit()
            }
            "addcontainer" -> {
                setTextHeader("Nuovo Contenitore")

                supportFragmentManager.beginTransaction()
                    .replace(fragment, ContainerFragment())
                    .commit()
            }
            "containerview" -> {
                setTextHeader("I Tuoi Container")

                supportFragmentManager.beginTransaction()
                    .replace(fragment, ContainerViewFragment())
                    .commit()
            }
            "addcamera" -> {
                setTextHeader("Nuovo Prodotto")
                val barcode = intent.getStringExtra("BARCODE")
                val name = intent.getStringExtra("NAME")
                val imageurl = intent.getStringExtra("IMAGEURL")

                var bundle = if (name != null && imageurl != null) {
                        bundleOf(
                        "SHOWBARCODE" to true,
                        "BARCODE" to barcode,
                        "NAME" to name,
                        "IMAGEURL" to imageurl
                    )
                } else {
                    bundleOf("SHOWBARCODE" to true, "BARCODE" to barcode)
                }

                val itemFragment = ItemFragment()
                itemFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(fragment, itemFragment)
                    .commit()
            }
        }
    }
}