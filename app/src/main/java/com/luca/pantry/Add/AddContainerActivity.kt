package com.luca.pantry.Add

import android.os.Bundle
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
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch

class AddContainerActivity : BaseActivity() {
    private val adapter = ContainerAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addcontainer)

        setTextHeader("Aggiungi contenitore")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_container)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        setupRecyclerView()

        setupAddcontainerButton()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun setupAddcontainerButton() {
        val add_container_button = findViewById<FloatingActionButton>(R.id.add_button_container)
        val a = "banana"
        var i = 0

        add_container_button.setOnClickListener {
            val nome = a + i.toString()
            val containerName = Container(
                nome
            )
            i++

            lifecycleScope.launch {
                PantryApp.database.containerDao().addContainer(containerName)
            }
            Toast.makeText(this, "Contenitore add premuto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val containers = PantryApp.database.containerDao().getAllContainers()
            adapter.updateData(containers)
        }
    }
}
