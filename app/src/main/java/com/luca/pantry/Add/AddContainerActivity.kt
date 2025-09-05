package com.luca.pantry.Add

import android.os.Bundle
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
import com.luca.pantry.ui.theme.PantryTheme
import com.luca.pantry.BaseActivity
import com.luca.pantry.R

class AddContainerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addcontainer)

        setTextHeader("Aggiungi contenitore")
    }

    override fun onResume() {
        super.onResume()

    }
}
