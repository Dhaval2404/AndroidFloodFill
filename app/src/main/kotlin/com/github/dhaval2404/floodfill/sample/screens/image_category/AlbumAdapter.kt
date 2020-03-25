package com.github.dhaval2404.floodfill.sample.screens.image_category

import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.databinding.AdapterAlbumBinding
import com.github.dhaval2404.floodfill.sample.model.Album
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class AlbumAdapter(private val viewModel: MainViewModel) :
    BaseAdapter<Album, AdapterAlbumBinding,
            AlbumAdapter.AlbumViewHolder>() {

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
                viewModel.onAlbumClick(it.tag as Album)
            }
        }
    }
}
