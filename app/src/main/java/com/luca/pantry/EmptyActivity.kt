package com.luca.pantry

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import com.luca.pantry.fragment.ContainerFragment
import com.luca.pantry.fragment.ContainerViewFragment
import com.luca.pantry.fragment.ExpiringItemsFragment
import com.luca.pantry.fragment.ItemFragment
import com.luca.pantry.fragment.ItemViewFragment

class EmptyActivity : BaseActivity() {
    private lateinit var origin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.empty_fragment)

            when (currentFragment) {
                is ContainerViewFragment -> setTextHeader("I Tuoi Container")
                is ItemViewFragment -> {
                    val name = currentFragment.arguments?.getString("CONTAINER") ?: "Prodotti"
                    setTextHeader(name)
                }
                is ExpiringItemsFragment -> setTextHeader("Prodotti in scadenza")
                is ContainerFragment -> setTextHeader("Nuovo Contenitore")
                is ItemFragment -> {
                    val modify = currentFragment.arguments?.getBoolean("MODIFYPRODUCT") ?: false
                    if (!modify) setTextHeader("Nuovo Prodotto") else setTextHeader("Modifica Prodotto")
                }
            }
        }

        loadFrameLayout()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
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
            "modifyproduct" -> {
                setTextHeader("Modifica Prodotto")

                val bundle = bundleOf(
                    "PRODUCTID" to intent.getStringExtra("PRODUCTID"),
                    "CONTAINERID" to intent.getStringExtra("CONTAINERID"),
                    "NAMEPRODUCT" to intent.getStringExtra("NAMEPRODUCT"),
                    "QUANTITYPRODUCT" to intent.getIntExtra("QUANTITYPRODUCT", 0),
                    "EXPIREDATEPRODUCT" to intent.getStringExtra("EXPIREDATEPRODUCT"),
                    "BARCODEPRODUCT" to intent.getStringExtra("BARCODEPRODUCT"),
                    "IMAGEPRODUCT" to intent.getStringExtra("IMAGEPRODUCT"),
                    "CREATEDATPRODUCT" to intent.getLongExtra("CREATEDATPRODUCT", 0L),
                    "MODIFYPRODUCT" to true,
                    "FROM" to intent.getStringExtra("FROM")
                )
                val itemFragment = ItemFragment()
                itemFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(fragment, itemFragment)
                    .commit()
            }
            "allitems" -> {
                setTextHeader("Prodotti")

                supportFragmentManager.beginTransaction()
                    .replace(fragment, ItemViewFragment())
                    .commit()
            }
            "expiringitems" -> {
                setTextHeader("Prodotti in scadenza")

                supportFragmentManager.beginTransaction()
                    .replace(fragment, ExpiringItemsFragment())
                    .commit()
            }
        }
    }
}