package com.github.dhaval2404.floodfill.sample.screens.drawing

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.github.dhaval2404.floodfill.FloodFill
import com.github.dhaval2404.floodfill.sample.util.BitmapUtil

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

    private val mDrawingViewChangeLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        DrawingHistory.setDrawingChangeListener {
            invalidate()
            mDrawingViewChangeLiveData.postValue(DrawingHistory.getIndex())
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
            val src = RectF(
                0f, 0f,
                it.width.toFloat(),
                it.height.toFloat()
            )

            val dst = RectF(0f, 0f, w.toFloat(), h.toFloat())

            mImageBitmapMatrix = Matrix().apply {
                setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)
            }
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

    private var mFloodFillRunning = false
    private fun touchUp(x: Float, y: Float) {
        if (mFloodFillRunning) return

        mFloodFillRunning = true
        val bitmap = overlay(mImageBitmap!!, mBitmap)

        val pixel = bitmap.getPixel(x.toInt(), y.toInt())
        if(BitmapUtil.isEqualColor(pixel, Color.BLACK)){
            Log.w("FloodFill", "Ignore black color")
            mFloodFillRunning = false
            return
        }

        Log.w("FloodFill", "Start")
        FloodFill.Builder(bitmap)
            .point(x, y)
            .newColor(mDrawColor)
            .build()
            .getPixels(Handler(Handler.Callback {
                Log.w("FloodFill", "Finish")
                val points = it.obj as FloatArray
                add(points)
                mFloodFillRunning = false
                true
            }))
    }

    private fun overlay(background: Bitmap, foreground: Bitmap): Bitmap {
        val combinedBitmap = Bitmap.createBitmap(foreground.width, foreground.height, foreground.config)
        val canvas = Canvas(combinedBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(background, mImageBitmapMatrix!!, mImagePaint)
        canvas.drawBitmap(foreground, 0f, 0f, mImagePaint)
        return combinedBitmap
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            touchUp(event.x, event.y)
            invalidate()
        }
        return true
    }

    /****** Getter/Setter Methods Starts ******/

    fun getDrawingViewChangeLiveData() = mDrawingViewChangeLiveData

    fun setBackgroundImage(bitmap: Bitmap) {
        this.mImageBitmap = bitmap
        init(width, height)
        invalidate()
    }

    fun setColor(color: Int) {
        this.mDrawColor = color
    }

    /****** Action Methods Starts ******/

    private fun add(points: FloatArray) {
        DrawingHistory.add(mDrawColor, points)
    }

    fun getBitmap(): Bitmap {
        val view = this

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

}
