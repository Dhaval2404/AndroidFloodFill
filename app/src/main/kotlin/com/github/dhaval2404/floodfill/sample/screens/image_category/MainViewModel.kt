package com.github.dhaval2404.floodfill.sample.screens.image_category

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dhaval2404.floodfill.sample.model.Album
import com.github.dhaval2404.floodfill.sample.model.AlbumImages
import com.github.dhaval2404.floodfill.sample.screens.image_picker.ImagePickerActivity
import com.github.dhaval2404.floodfill.sample.util.AssetUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.util.*

/**
 * TODO: Add Class Header
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 27 Dec 2019
 */
class MainViewModel : ViewModel(), KoinComponent {

    private var mNavigator: MainNavigator? = null
    private val mContext:Context by inject()

    private val mAlbumAdapter: AlbumAdapter by lazy {
        AlbumAdapter(this)
    }

    init {
        loadAlbums()
    }

    fun setNavigator(navigator: MainNavigator) {
        this.mNavigator = navigator
    }

    fun getAdapter() = mAlbumAdapter

    fun onAlbumClick(album: Album) {
        val intent = ImagePickerActivity.getIntent(mContext, album)
        this.mNavigator?.startActivity(intent)
    }

    private fun loadAlbums(){
        viewModelScope.launch {
            val albums = withContext(Dispatchers.IO){
                readAlbumImages(mContext)
            }
            mAlbumAdapter.refresh(albums)
        }
    }

    /*
     * List Asset files
     */
    private fun readAlbumImages(context: Context): List<Album> {
        try {
            val json = AssetUtil.readFile(context, "albums_images.json")
            return Gson().fromJson(json, AlbumImages::class.java).albums
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return Collections.emptyList()
    }

}
