package com.luca.pantry.Add

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.luca.pantry.BaseActivity
import com.luca.pantry.EmptyActivity
import com.luca.pantry.R
import com.luca.pantry.fragment.CameraFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class AddItemActivity : BaseActivity(), CameraFragment.BarcodeCallback {

    protected lateinit var add_manually_btn: MaterialButton
    protected lateinit var cancel_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_additem)

        setTextHeader("Aggiungi prodotto")

        // Carica subito la CameraView
        supportFragmentManager.beginTransaction()
            .replace(R.id.camera_fragment, CameraFragment())
            .commit()

        buttonAnimationConfig()
        setupCancelButton()
        setupAddmanuallyButton()
    }

    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig(){
        add_manually_btn = findViewById(R.id.btn_add_manually)
        cancel_btn = findViewById(R.id.btn_cancel)

        add_manually_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }

        cancel_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = android.view.animation.AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }
    }

    private fun setupCancelButton() {
        cancel_btn = findViewById(R.id.btn_cancel)

        cancel_btn.setOnClickListener {
            finish()
        }
    }

    private fun setupAddmanuallyButton() {
        add_manually_btn = findViewById(R.id.btn_add_manually)
        var addmanually = "addmanually"

        add_manually_btn.setOnClickListener {
            val intent = Intent(this, EmptyActivity::class.java).apply {
                putExtra("ORIGIN", addmanually)
            }
            startActivity(intent)
        }
    }

    override fun onBarcodeScanned(code: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://world.openfoodfacts.org/api/v0/product/$code.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("OpenFoodFacts", "Errore: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val json = JSONObject(body)
                val status = json.optInt("status")

                val intent = Intent(this@AddItemActivity, EmptyActivity::class.java).apply {
                    putExtra("ORIGIN", "addcamera")
                    putExtra("BARCODE", code)

                    if (status == 1) {
                        val product = json.getJSONObject("product")
                        val name = product.optString("product_name")
                        val imageUrl = product.optString("image_url")

                        putExtra("NAME", name)
                        putExtra("IMAGEURL", imageUrl)
                    }
                }

                runOnUiThread {
                    if (status != 1) {
                        Toast.makeText(this@AddItemActivity, "Prodotto non trovato", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(intent)
                }
            }
        })
    }
}