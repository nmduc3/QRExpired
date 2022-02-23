package com.example.myapplication.main.qrscan.scan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.example.myapplication.main.extension.isPortrait
import com.example.myapplication.main.extension.toPx
import kotlin.math.min


class ScannerOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val transparentPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private val strokePaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            //strokeWidth = Utils.dpToPx(3.0f, context).toFloat()
            style = Paint.Style.FILL
        }
    }

    var qrCodeDetected: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        setWillNotDraw(false)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            setLayerType(LAYER_TYPE_HARDWARE, null)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#88000000"))

        val radius = 2.toPx().toFloat()
        canvas.drawRoundRect(scanRect, radius, radius, transparentPaint)

        strokePaint.color = if (qrCodeDetected) Color.BLUE else Color.WHITE

        val scanSize = scanRect.right - scanRect.left
        val cornerWidth = (scanSize * 0.2).toInt()
        val cornerLength = 3.toPx()
        // top - left
        canvas.drawRect(
            scanRect.left - cornerLength,
            scanRect.top - cornerLength,
            scanRect.left + cornerWidth,
            scanRect.top,
            strokePaint
        )
        canvas.drawRect(
            scanRect.left - cornerLength,
            scanRect.top - cornerLength,
            scanRect.left,
            scanRect.top + cornerWidth,
            strokePaint
        )

        // top - right
        canvas.drawRect(
            scanRect.right - cornerWidth,
            scanRect.top - cornerLength,
            scanRect.right + cornerLength,
            scanRect.top,
            strokePaint
        )
        canvas.drawRect(
            scanRect.right,
            scanRect.top - cornerLength,
            scanRect.right + cornerLength,
            scanRect.top + cornerWidth,
            strokePaint
        )

        // bot - left
        canvas.drawRect(
            scanRect.right - cornerWidth,
            scanRect.bottom,
            scanRect.right + cornerLength,
            scanRect.bottom + cornerLength,
            strokePaint
        )
        canvas.drawRect(
            scanRect.right,
            scanRect.bottom - cornerWidth,
            scanRect.right + cornerLength,
            scanRect.bottom + cornerLength,
            strokePaint
        )

        // bot - right
        canvas.drawRect(
            scanRect.left - cornerLength,
            scanRect.bottom,
            scanRect.left + cornerWidth,
            scanRect.bottom + cornerLength,
            strokePaint
        )
        canvas.drawRect(
            scanRect.left - cornerLength,
            scanRect.bottom - cornerWidth,
            scanRect.left,
            scanRect.bottom + cornerLength,
            strokePaint
        )
    }

    val size: Size
        get() = Size(width, height)

    val scanRect: RectF
        get() =
            if (context.isPortrait()) {
                val size = min(width * 0.5f, MAX_WIDTH_PORTRAIT)
                val l = (width - size) / 2
                val r = l + size
                val t = (height - size) / 2
                val b = t + size
                RectF(l, t, r, b)
            } else {
                val size = min(width * 0.25f, MAX_WIDTH_LANDSCAPE)
                val l = width * 0.05f
                val r = l + size
                val t = height * 0.05f
                val b = t + size
                RectF(l, t, r, b)
            }

    companion object {
        const val MAX_WIDTH_PORTRAIT = 1200f
        const val MAX_WIDTH_LANDSCAPE = 1600f
    }
}