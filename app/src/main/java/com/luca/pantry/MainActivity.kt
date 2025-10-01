package com.luca.pantry

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.Adapter.ProdottoAdapter
import com.luca.pantry.Add.AddItemActivity
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.EntityDB.Prodotto
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : BaseActivity() {
    private lateinit var adapter: ProdottoAdapter
    private lateinit var add_button: ImageButton

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

        val recyclerView = findViewById<RecyclerView>(R.id.home_expiring_items)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProdottoAdapter(
            prodotti = emptyList(),
            onRename = { prodotto -> val intent = Intent(this, EmptyActivity::class.java).apply {
                putExtra("ORIGIN", "modifyproduct")
                putExtra("NAMEPRODUCT", prodotto.productName)
                putExtra("QUANTITYPRODUCT", prodotto.quantity)
                putExtra("EXPIREDATEPRODUCT", prodotto.expiringDate)
                putExtra("CONTAINERPRODUCT", prodotto.container)
                putExtra("BARCODEPRODUCT", prodotto.barcode)
                putExtra("IMAGEPRODUCT", prodotto.imageUrl)
                putExtra("FROM", "new")
                }
                startActivity(intent)
            },
            onChangeQuantity = { prodotto -> showQuantityDialog(prodotto) },
            onDelete = { prodotto -> deleteProduct(prodotto) }
        )
        recyclerView.adapter = adapter
        setupRecyclerView()

        buttonAnimationConfig()
        popupMenuCreate()

    }



    override fun onResume() {
        super.onResume()

        setupRecyclerView()
    }

    private fun showQuantityDialog(prodotto: Prodotto) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(prodotto.quantity.toString())
        }

        AlertDialog.Builder(this)
            .setTitle("Modifica quantità")
            .setView(input)
            .setPositiveButton("Salva") { _, _ ->
                val newQuantity = input.text.toString().toIntOrNull()
                if (newQuantity != null) {
                    lifecycleScope.launch {
                        val updated = prodotto.copy(quantity = newQuantity)
                        PantryApp.database.prodottoDao().update(updated)
                        setupRecyclerView()
                    }
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun deleteProduct(prodotto: Prodotto) {
        AlertDialog.Builder(this)
            .setTitle("Elimina prodotto")
            .setMessage("Sei sicuro di voler eliminare ${prodotto.productName}?")
            .setPositiveButton("Elimina") {_, _ ->
                lifecycleScope.launch {
                    PantryApp.database.prodottoDao().deleteProduct(prodotto)
                    setupRecyclerView()
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    //Create a pop-up menu for the add button (Add item/ add container)
    private fun popupMenuCreate(){
        add_button = findViewById(R.id.add_button_home)
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
                        val addcontainer = "addcontainer"
                        val intent = Intent(this, EmptyActivity::class.java).apply {
                            putExtra("ORIGIN", addcontainer)
                        }
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    private fun setupRecyclerView() {
        val dao = PantryApp.database.prodottoDao()

        lifecycleScope.launch {
            val products = dao.getAllItems()
            val today = Date()
            val limit = Calendar.getInstance().apply {
                time = today
                add(Calendar.DAY_OF_YEAR, 15)
            }.time

            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)

            val expiring_items = products.filter {
                val expire_date = formatter.parse(it.expiringDate)
                expire_date != null && expire_date.before(limit)
            }

            adapter.updateData(expiring_items)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig() {
        add_button = findViewById(R.id.add_button_home)

        add_button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }
    }
}