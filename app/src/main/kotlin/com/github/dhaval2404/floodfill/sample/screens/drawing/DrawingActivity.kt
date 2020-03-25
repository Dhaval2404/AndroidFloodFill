package com.github.dhaval2404.floodfill.sample.screens.drawing

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.dhaval2404.floodfill.sample.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_drawing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 11 Dec 2019
 */
class DrawingActivity : AppCompatActivity() {

    private val mViewModel: DrawingViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        colorPaletteRV.adapter = mViewModel.getColorPaletteAdapter()
        colorShadeRV.adapter = mViewModel.getColorShadeAdapter()
        colorShadeRV.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.NOWRAP)
        colorShadeRV.isNestedScrollingEnabled = false

        mViewModel.loadColors(this)
        mViewModel.setColorChangeListener { color ->
            drawView.setColor(color)
        }

        drawView.getDrawingViewChangeLiveData().observe(this, Observer<Int> {
            undoImg.alpha = if (DrawingHistory.canUndo()) 1f else 0.3f
            redoImg.alpha = if (DrawingHistory.canRedo()) 1f else 0.3f
        })

        setDrawingImage()
    }

    private fun setDrawingImage() {
        val path = intent?.getStringExtra("path")
        if (path == null) {
            Toast.makeText(this, "Invalid Image", Toast.LENGTH_LONG).show()
            onBackPressed()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                getAssetImageBitmap(applicationContext, path)
            }?.let {
                drawView.setBackgroundImage(it)
            }
        }
    }

    private fun getAssetImageBitmap(context: Context, file: String): Bitmap? {
        return Glide.with(context)
            .asBitmap()
            .load(Uri.parse(file))
            .submit().get()
    }

    public fun undo(view: View) {
        DrawingHistory.undo()
    }

    public fun redo(view: View) {
        DrawingHistory.redo()
    }

    public fun save(view: View) {
        checkStoragePermission {
            mViewModel.saveDrawing(this, drawView.getBitmap())
        }
    }

    private fun checkStoragePermission(listener: () -> Unit) {
        TedPermission.with(this)
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    listener.invoke()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }
            })
            .setDeniedMessage("If you reject permission, you can not export drawing.\n\nPlease turn on permissions at [Setting] > [Permission]")
            .check()
    }

    public fun reset(view: View) {
        AlertDialog.Builder(this)
            .setMessage("Empty Canvas?")
            .setPositiveButton("Yes") { dialog, which ->
                DrawingHistory.reset()
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }
}
