package com.luca.pantry.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.luca.pantry.R
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.luca.pantry.Add.AddItemActivity

class CameraFragment : Fragment() {
    val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var previewView: PreviewView

    private var callback: BarcodeCallback? = null

    private var hasHandlerBarcode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previewView = view.findViewById(R.id.preview_view)


        //Verify camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    interface BarcodeCallback {
        fun onBarcodeScanned(code: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BarcodeCallback) {
            callback = context
        }
    }

    private fun handleBarcode(rawValue: String) {
        if (hasHandlerBarcode) return

        hasHandlerBarcode = true
        callback?.onBarcodeScanned(rawValue)

        Handler(Looper.getMainLooper()).postDelayed({
            hasHandlerBarcode = false
        }, 4000)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val safeContext = context ?: return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
        cameraProviderFuture.addListener({
            if (!isAdded || view == null) return@addListener

            val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_DATA_MATRIX
            ).build()

            val cameraProvider = cameraProviderFuture.get()

            val previewUseCase = Preview.Builder()
                .build()
                .also { it.surfaceProvider = previewView.surfaceProvider }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            var lastLongTime = 0L
            var lastScannedCode: String? = null

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(safeContext)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    scanner.process(inputImage).addOnSuccessListener { barcodes ->
                        val now = System.currentTimeMillis()
                            if (now - lastLongTime > 1000) {
                                for (barcode in barcodes) {
                                    val rawValue = barcode.rawValue

                                    if (rawValue != null && rawValue != lastScannedCode) {
                                        lastScannedCode = rawValue
                                        handleBarcode(rawValue) //passa il codice a barre al fragment

                                        Handler(Looper.getMainLooper()).postDelayed({
                                            lastScannedCode = null
                                        }, 4000)
                                    }

                                    //Toast.makeText(safeContext, rawValue, Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                        .addOnFailureListener {
                            Toast.makeText(safeContext, "Error", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Toast.makeText(safeContext, "Errore fotocamera", Toast.LENGTH_SHORT).show()
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(safeContext))
    }
}