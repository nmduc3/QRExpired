package com.example.myapplication.main.qrscan.scan

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutionException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class QRScanner(
    val appContext: Context,
    val appLifeCycle: LifecycleOwner,
    val onSuccessListenerCallback: ((String) -> Unit)?
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: QrScannerProcessor? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    private var previewView: PreviewView? = null
    private lateinit var scannerOverLay: ScannerOverlay
    private var isFullScan: Boolean = false
    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    val cameraProviderFuture =
        ProcessCameraProvider.getInstance(appContext)

    fun setupCamera(
        preview: PreviewView?,
        overlay: ScannerOverlay,
        isFullScan: Boolean? = false
    ) {
        initView(preview, overlay, isFullScan!!)
        cameraProviderFuture.addListener(
            Runnable {
                try {
                    // Camera provider is now guaranteed to be available - from AndroidDeveloper document
                    cameraProvider = cameraProviderFuture.get()
                    bindCameraUseCases()
                } catch (e: ExecutionException) {
                    // Handle any errors (including cancellation) here.
                    Log.e(TAG, "QRScanner setupCamera ExecutionException: ${e}")
                } catch (e: InterruptedException) {
                    Log.e(TAG, "QRScanner setupCamera InterruptedException: ${e}")
                }
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

    private fun initView(preview: PreviewView?, overlay: ScannerOverlay, isFullScanImage: Boolean) {
        previewView = preview
        scannerOverLay = overlay
        isFullScan = isFullScanImage
        scannerOverLay.qrCodeDetected = false
    }

    fun bindCameraUseCases() {
        if (cameraProvider != null) {
            bindPreviewUseCase()
            bindAnalyseUseCase()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreviewUseCase() {
        cameraProvider?.let { camProvider ->

            if (previewUseCase != null) {
                camProvider.unbind(previewUseCase)
            }

            cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            previewView?.let { preview ->
                preview.display?.let { display ->
                    cameraSelector?.let {

                        previewUseCase = Preview.Builder()
                            .setTargetAspectRatio(screenAspectRatio)
                            .setTargetRotation(display.rotation)
                            .setCameraSelector(it)
                            .build()
                        previewUseCase?.let {
                            it.setSurfaceProvider(preview.surfaceProvider)
                        }
                    }
                }

                try {
                    cameraSelector?.let {
                        camProvider.bindToLifecycle(
                            appLifeCycle,
                            it,
                            previewUseCase
                        )
                    }
                } catch (illegalStateException: IllegalStateException) {
                    Log.e(TAG, illegalStateException.message.toString())
                } catch (illegalArgumentException: IllegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.message.toString())
                }
            }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun bindAnalyseUseCase() {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.

        if (cameraProvider == null) {
            return
        }

        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }
        imageProcessor = QrScannerProcessor(
            appContext,
            onSuccessListenerCallback
        )

        previewView?.let { preview ->
            preview.display?.let {
                analysisUseCase = ImageAnalysis.Builder()
                    .setTargetAspectRatio(screenAspectRatio)
                    .setTargetRotation(it.rotation)
                    .build()
            }
        }

        analysisUseCase?.setAnalyzer(
            ContextCompat.getMainExecutor(appContext),
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ImageAnalysis.Analyzer { imageProxy: ImageProxy ->
                try {
                    imageProcessor?.let { processor ->
                        imageProxy.image?.let { image ->
                            processor.processImageProxy(
                                image,
                                imageProxy.imageInfo.rotationDegrees,
                                scannerOverLay,
                                isFullScan
                            )
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        }
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                    imageProxy.close()
                }
            }
        )

        try {
            cameraProvider!!.bindToLifecycle(
                appLifeCycle,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message.toString())
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message.toString())
        }
    }

    fun stop() {
        imageProcessor?.run {
            this.stop()
        }
    }

    fun release() {
        cameraProvider?.unbindAll()
        previewView = null
        cameraProvider = null
        previewUseCase = null
        analysisUseCase = null
        imageProcessor = null
        cameraSelector = null
    }

    companion object {
        private val TAG = "QR_SCAN"

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}