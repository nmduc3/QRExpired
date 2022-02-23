package com.example.myapplication.main.qrscan.scan

import android.graphics.Bitmap
import android.media.Image
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import java.nio.ByteBuffer

interface ImageProcessor<T> {
    /** Processes a bitmap image.  */
    fun processBitmap(bitmap: Bitmap?)

    /** Processes ByteBuffer image data, e.g. used for Camera1 live preview case.  */
    @Throws(MlKitException::class)
    fun processByteBuffer(
        data: ByteBuffer?, frameMetadata: FrameMetadata?
    )

    /** Processes ImageProxy image data, e.g. used for CameraX live preview case.  */
    @Throws(MlKitException::class)
    fun processImageProxy(
        image: Image,
        rotation: Int,
        scannerOverlay: ScannerOverlay,
        isFullScan: Boolean
    ): Task<T>

    /** Stops the underlying machine learning model and release resources.  */
    fun stop()
}