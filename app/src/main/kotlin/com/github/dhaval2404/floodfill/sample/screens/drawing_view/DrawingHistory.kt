package com.github.dhaval2404.floodfill.sample.screens.drawing_view

import com.github.dhaval2404.floodfill.sample.data.model.DrawMove

object DrawingHistory {

    private var mDrawingIndex = 0
    private val mDrawingHistory: MutableList<DrawMove> = ArrayList<DrawMove>()
    private var mDrawStarted = false

    private var mDrawingChangeListener: MutableList<(() -> Unit)> = ArrayList()

    fun getIndex() = mDrawingIndex

    fun getHistory(): List<DrawMove> = mDrawingHistory

    fun addDrawingChangeListener(listener: () -> Unit) {
        this.mDrawingChangeListener.add(listener)
    }

    fun removeDrawingChangeListener(listener: () -> Unit) {
        this.mDrawingChangeListener.remove(listener)
    }

    fun add(color: Int, points: FloatArray) {
        if (points.isEmpty()) return

        if (mDrawingHistory.size > mDrawingIndex) {
            mDrawingHistory.subList(mDrawingIndex, mDrawingHistory.size).clear()
        }

        mDrawingHistory.add(DrawMove(color, points))
        mDrawingIndex++

        mDrawStarted = true

        handleDrawingChange()
    }

    fun canUndo() = mDrawingIndex > 0

    fun undo() {
        if (canUndo()) {
            mDrawingIndex--
            handleDrawingChange()
        }
    }

    fun canRedo() = mDrawingIndex < mDrawingHistory.size

    fun redo() {
        if (canRedo()) {
            mDrawingIndex++
            handleDrawingChange()
        }
    }

    fun isDrawStarted() = mDrawStarted

    fun reset() {
        mDrawingIndex = 0
        mDrawStarted = false
        mDrawingHistory.clear()
        handleDrawingChange()
    }

    private fun handleDrawingChange() {
        mDrawingChangeListener.forEach {
            it.invoke()
        }
    }

}