package com.github.dhaval2404.floodfill.sample.screens.drawing_view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.github.dhaval2404.floodfill.FloodFill
import com.github.dhaval2404.floodfill.sample.util.BitmapUtil
import com.github.dhaval2404.floodfill.sample.util.ColorUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 11 Dec 2019
 */
class DrawingView : View {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private lateinit var mBitmap: Bitmap

    private var mImageBitmap: Bitmap? = null
    private var mImagePaint = Paint(Paint.DITHER_FLAG)
    private var mImageBitmapMatrix: Matrix? = null

    private var mDrawColor: Int = Color.parseColor("#5c6bc0")

    // Mutex
    private val mutex = Mutex()

    init {
        FloodFill.init()

        DrawingHistory.addDrawingChangeListener {
            invalidate()
        }
    }

    private val mPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init(w, h)
    }

    private fun init(w: Int, h: Int) {
        if (w <= 0 || h <= 0) {
            Log.w("TAG", "w=0, h=0")
            return
        }

        mImageBitmap?.let {
            mImageBitmapMatrix = BitmapUtil.getBitmapMatrix(it, w.toFloat(), h.toFloat())
        }

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mImageBitmap != null) {
            canvas.drawBitmap(mImageBitmap!!, mImageBitmapMatrix!!, mImagePaint)
        }

        for (i in 0 until DrawingHistory.getIndex()) {
            val drawMove = DrawingHistory.getHistory()[i]
            mPaint.color = drawMove.color
            canvas.drawPoints(drawMove.points, mPaint)
        }
    }

    private fun touchUp(x: Float, y: Float) {
        Log.w("FloodFill", "Start")
        CoroutineScope(Dispatchers.Main).launch {
            val points: FloatArray? = withTimeoutOrNull(TimeUnit.SECONDS.toMillis(10)) {
                applyFloodFill(x, y)
            }
            if (points != null && points.isNotEmpty()) {
                Log.w("FloodFill", "Finish")
                DrawingHistory.add(mDrawColor, points)
                invalidate()
            }
        }
    }

    private suspend fun applyFloodFill(x: Float, y: Float): FloatArray? {
        val bitmap = overlay(mImageBitmap!!, mBitmap)

        val pixel = bitmap.getPixel(x.toInt(), y.toInt())
        if (ColorUtil.isEqualColor(pixel, Color.BLACK)) {
            Log.w("FloodFill", "Ignore black color")
            return null
        }

        val floodFill = FloodFill.Builder(bitmap)
            .point(x, y)
            .newColor(mDrawColor)
            .build()

        val points = withContext(Dispatchers.IO) {
            // Synchronization Alternative
            mutex.withLock {
                floodFill.getPixels()
            }
        }

        // Release Memory
        bitmap.recycle()

        return points
    }

    private fun overlay(background: Bitmap, foreground: Bitmap): Bitmap {
        val combinedBitmap =
            Bitmap.createBitmap(foreground.width, foreground.height, foreground.config)
        val canvas = Canvas(combinedBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(background, mImageBitmapMatrix!!, mImagePaint)
        canvas.drawBitmap(foreground, 0f, 0f, mImagePaint)
        return combinedBitmap
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchUp(event.x, event.y)
        }
        return true
    }

    /****** Getter/Setter Methods Starts ******/

    fun setBackgroundImage(bitmap: Bitmap) {
        this.mImageBitmap = bitmap
        init(width, height)
        invalidate()
    }

    fun setColor(color: Int) {
        this.mDrawColor = color
    }

    fun getBitmap() = BitmapUtil.getBitmap(this)

}
