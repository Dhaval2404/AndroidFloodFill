package com.github.dhaval2404.floodfill.sample.screens.album_picker

import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.data.model.Album
import com.github.dhaval2404.floodfill.sample.databinding.AdapterAlbumBinding
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * Album Picker Adapter
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class AlbumPickerAdapter : BaseAdapter<Album, AdapterAlbumBinding,
        AlbumPickerAdapter.AlbumViewHolder>() {

    private var mAlbumClickListener: ((Album) -> Unit)? = null

    fun setAlbumClickListener(listener: ((Album) -> Unit)) {
        mAlbumClickListener = listener
    }

    override fun getLayout() = R.layout.adapter_album

    override fun getViewHolder(binding: AdapterAlbumBinding) =
        AlbumViewHolder(binding)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.binding.album = album
    }

    inner class AlbumViewHolder(val binding: AdapterAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageLyt.setOnClickListener {
                mAlbumClickListener?.invoke(it.tag as Album)
            }
        }
    }
}
