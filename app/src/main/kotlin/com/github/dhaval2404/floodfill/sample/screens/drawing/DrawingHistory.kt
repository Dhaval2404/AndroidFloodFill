package com.github.dhaval2404.floodfill.sample.screens.drawing

import com.github.dhaval2404.floodfill.sample.model.DrawMove

object DrawingHistory {

    private var mDrawingIndex = 0
    private val mDrawingHistory: MutableList<DrawMove> = ArrayList<DrawMove>()
    private var mDrawingChangeListener: (() -> Unit)? = null

    fun getIndex()  = mDrawingIndex

    fun getHistory():List<DrawMove>  = mDrawingHistory



    fun setDrawingChangeListener(listener: () -> Unit) {
        this.mDrawingChangeListener = listener
    }

    fun add(color: Int, points: FloatArray) {
        if (points.isEmpty()) return

        if (mDrawingHistory.size > mDrawingIndex) {
            mDrawingHistory.subList(mDrawingIndex, mDrawingHistory.size).clear()
        }

        mDrawingHistory.add(DrawMove(color, points))
        mDrawingIndex++

        mDrawingChangeListener?.invoke()
    }

    fun canUndo() = mDrawingIndex > 0

    fun undo() {
        if (canUndo()) {
            mDrawingIndex--
            mDrawingChangeListener?.invoke()
        }
    }

    fun canRedo() = mDrawingIndex < mDrawingHistory.size

    fun redo() {
        if (canRedo()) {
            mDrawingIndex++
            mDrawingChangeListener?.invoke()
        }
    }

    fun reset() {
        mDrawingIndex = 0
        mDrawingHistory.clear()
        mDrawingChangeListener?.invoke()
    }

}