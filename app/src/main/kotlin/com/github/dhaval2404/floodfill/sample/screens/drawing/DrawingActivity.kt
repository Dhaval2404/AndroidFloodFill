package com.github.dhaval2404.floodfill.sample.screens.drawing

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.floodfill.sample.R
import kotlinx.android.synthetic.main.content_drawing.*

class DrawingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
        setDrawingImage()
    }

    private fun setDrawingImage() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_flower_01)
        drawView.setBackgroundImage(bitmap)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_undo -> drawView.undo()
            R.id.action_redo -> drawView.redo()
        }
        return true
    }*/
}
