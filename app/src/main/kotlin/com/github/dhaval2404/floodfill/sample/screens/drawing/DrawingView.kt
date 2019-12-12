package com.github.dhaval2404.floodfill.sample.screens.drawing

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.github.dhaval2404.floodfill.sample.model.DrawMove

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
    private lateinit var mCanvas: Canvas

    private var mImageBitmap: Bitmap? = null
    private var mImagePaint = Paint(Paint.DITHER_FLAG)
    private var mImageBitmapMatrix: Matrix? = null

    private var mDrawingIndex = 0
    private val mDrawingHistory: MutableList<DrawMove> = ArrayList()
    private var mDrawColor: Int = Color.BLUE

    private val mPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = mDrawColor
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
        mCanvas = Canvas(mBitmap)
        mCanvas.drawColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(!::mBitmap.isInitialized){
            return
        }

        if (mImageBitmap != null) {
            canvas.drawBitmap(mImageBitmap!!, mImageBitmapMatrix!!, mImagePaint)
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, null)

        for (i in 0 until mDrawingIndex) {
            val drawMove = mDrawingHistory[i]
            mPaint.color = drawMove.color
            canvas.drawPoints(drawMove.points, mPaint)
        }
    }

    private fun touchUp(x: Float, y: Float) {
        val bitmap = overlay(mImageBitmap!!, mBitmap)
        /*FloodFill.Builder(bitmap)
            .point(x, y)
            .newColor(mDrawColor)
            .build()
            .getPixels(Handler(Handler.Callback {
                val points = it.obj as FloatArray
                add(points)
                true
            }))*/
    }

    private fun overlay(background: Bitmap, foreground: Bitmap): Bitmap {
        val combinedBitmap =
            Bitmap.createBitmap(foreground.width, foreground.height, foreground.config)
        val canvas = Canvas(combinedBitmap)
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

    private fun add(points: FloatArray) {
        if (points.isEmpty()) return

        if (mDrawingHistory.size > mDrawingIndex) {
            mDrawingHistory.subList(mDrawingIndex, mDrawingHistory.size).clear()
        }

        mDrawingHistory.add(DrawMove(mDrawColor, points))
        mDrawingIndex++

        invalidate()
    }

    fun undo() {
        if (mDrawingIndex > 0) {
            mDrawingIndex--
            invalidate()
        }
    }

    fun redo() {
        if (mDrawingIndex < mDrawingHistory.size) {
            mDrawingIndex++
            invalidate()
        }
    }

    fun setBackgroundImage(bitmap: Bitmap) {
        this.mImageBitmap = bitmap
        init(width, height)
        invalidate()
    }

}
