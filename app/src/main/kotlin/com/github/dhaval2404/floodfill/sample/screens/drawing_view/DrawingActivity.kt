package com.github.dhaval2404.floodfill.sample.screens.drawing_view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import com.github.dhaval2404.floodfill.sample.screens.base.BaseActivity
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
import org.koin.android.ext.android.get

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 11 Dec 2019
 */
class DrawingActivity : BaseActivity<DrawingViewModel>(R.layout.activity_drawing) {

    override fun getViewModel(): DrawingViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DrawingHistory.reset()

        colorPaletteRV.adapter = mViewModel.getColorPaletteAdapter()
        colorShadeRV.adapter = mViewModel.getColorShadeAdapter()
        colorShadeRV.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.NOWRAP)
        colorShadeRV.isNestedScrollingEnabled = false

        mViewModel.getColorPaletteAdapter().colorPaletteLiveData
            .observe(this, Observer {
                mViewModel.setRecentColorPalette(it.palette)
                mViewModel.getColorShadeAdapter().refresh(it.shades)
            })

        mViewModel.getColorShadeAdapter().colorShadeLiveData
            .observe(this, Observer {
                mViewModel.setRecentColor(it)
                drawView.setColor(it)
            })

        DrawingHistory.addDrawingChangeListener {
            undoImg.alpha = if (DrawingHistory.canUndo()) 1f else 0.3f
            redoImg.alpha = if (DrawingHistory.canRedo()) 1f else 0.3f
        }

        setDrawingImage()
    }

    private fun setDrawingImage() {
        val image = intent?.getParcelableExtra<Image>(EXTRA_IMAGE)
        val imagePath = intent?.getStringExtra(EXTRA_IMAGE_PATH)

        val path = imagePath ?: image?.localPath ?: image?.url
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

    fun undo(view: View) {
        DrawingHistory.undo()
    }

    fun redo(view: View) {
        DrawingHistory.redo()
    }

    fun save(view: View) {
        checkStoragePermission {
            mViewModel.exportDrawing(this, drawView.getBitmap())
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

    fun reset(view: View) {
        AlertDialog.Builder(this)
            .setMessage(R.string.title_empty_canvas)
            .setPositiveButton(R.string.action_yes) { _, _ ->
                reset()
            }
            .setNegativeButton(R.string.action_no, null)
            .show()
    }

    private fun reset() {
        DrawingHistory.reset()
        intent?.getParcelableExtra<Image>(EXTRA_IMAGE)?.let {
            mViewModel.reset(it)
            setDrawingImage()
        }
    }

    override fun onPause() {
        super.onPause()
        if (DrawingHistory.isDrawStarted()) {
            val image = intent?.getParcelableExtra<Image>(EXTRA_IMAGE)
            mViewModel.saveDrawing(this, image, drawView.getBitmap())
        }
    }

    companion object {

        private const val EXTRA_IMAGE = "extra.image"
        private const val EXTRA_IMAGE_PATH = "extra.image_path"

        fun getIntent(context: Context, image: Image): Intent {
            return Intent(context, DrawingActivity::class.java).apply {
                putExtra(EXTRA_IMAGE, image)
            }
        }

        fun getIntent(context: Context, path: String): Intent {
            return Intent(context, DrawingActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_PATH, path)
            }
        }

    }

}
