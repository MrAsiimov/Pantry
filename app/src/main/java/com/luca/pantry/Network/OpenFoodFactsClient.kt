package com.luca.pantry.Network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

data class OffProduct(val name: String?, val imageUrl: String?)

sealed class OffResult {
    data class Found(val product: OffProduct) : OffResult()
    object NotFound : OffResult()
    object NetworkError : OffResult()
}

object OpenFoodFactsClient {
    private val client = OkHttpClient.Builder()
        .callTimeout(6, TimeUnit.SECONDS)
        .build()

    suspend fun lookupDetailed(barcode: String): OffResult = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://world.openfoodfacts.org/api/v2/product/$barcode.json?fields=product_name,image_url")
            .header("User-Agent", "Pantry/1.0 (Android)")
            .build()
        try {
            client.newCall(request).execute().use { response ->
                if (response.code == 404) return@use OffResult.NotFound
                if (!response.isSuccessful) return@use OffResult.NetworkError
                val body = response.body?.string() ?: return@use OffResult.NetworkError
                val json = JSONObject(body)
                if (json.optInt("status") != 1) return@use OffResult.NotFound
                val product = json.optJSONObject("product") ?: return@use OffResult.NotFound
                OffResult.Found(
                    OffProduct(
                        name = product.optString("product_name").trim().ifEmpty { null },
                        imageUrl = product.optString("image_url").trim().ifEmpty { null }
                    )
                )
            }
        } catch (e: IOException) {
            OffResult.NetworkError
        } catch (e: JSONException) {
            OffResult.NetworkError
        }
    }

    suspend fun lookup(barcode: String): OffProduct? =
        (lookupDetailed(barcode) as? OffResult.Found)?.product
}