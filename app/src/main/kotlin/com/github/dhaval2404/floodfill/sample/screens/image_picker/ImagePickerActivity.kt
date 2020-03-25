package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.model.Album
import kotlinx.android.synthetic.main.activity_image_picker.*
import org.koin.android.ext.android.inject

class ImagePickerActivity : AppCompatActivity(), ImagePickerNavigator {

    private val mViewModel: ImagePickerViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        mViewModel.setNavigator(this)
        imageRV.adapter = mViewModel.getAdapter()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val album = intent?.getParcelableExtra<Album>(EXTRA_ALBUM)

        toolbar.title = album?.title

        album?.images?.let {
            mViewModel.getAdapter().refresh(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_ALBUM = "extra.album"

        fun getIntent(context: Context, album: Album): Intent {
            return Intent(context, ImagePickerActivity::class.java).apply {
                putExtra(EXTRA_ALBUM, album)
            }
        }

    }

}
