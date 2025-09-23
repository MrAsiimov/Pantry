package com.luca.pantry.Add

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luca.pantry.Adapter.ContainerAdapter
import com.luca.pantry.ui.theme.PantryTheme
import com.luca.pantry.BaseActivity
import com.luca.pantry.EmptyActivity
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch

class AddContainerActivity : BaseActivity() {
    private val adapter = ContainerAdapter(emptyList())

    private lateinit var add_container_btn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addcontainer)

        setTextHeader("Aggiungi contenitore")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_container)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        setupRecyclerView()

        buttonAnimationConfig()
        setupAddcontainerButton()
    }

    override fun onResume() {
        super.onResume()

        setupRecyclerView()
    }

    private fun setupAddcontainerButton() {
        add_container_btn = findViewById(R.id.add_button_container)
        var addcontainer = "addcontainer"

        add_container_btn.setOnClickListener {
            val intent = Intent(this, EmptyActivity::class.java).apply {
                putExtra("ORIGIN", addcontainer)
            }
            startActivity(intent)
        }
    }
    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val containers = PantryApp.database.containerDao().getAllContainers()
            adapter.updateData(containers)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig() {
        add_container_btn = findViewById(R.id.add_button_container)

        add_container_btn.setOnTouchListener { v, event ->
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
