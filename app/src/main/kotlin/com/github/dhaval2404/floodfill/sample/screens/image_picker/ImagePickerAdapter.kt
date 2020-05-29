package com.github.dhaval2404.floodfill.sample.screens.image_picker

import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import com.github.dhaval2404.floodfill.sample.databinding.AdapterImagePickerBinding
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class ImagePickerAdapter : BaseAdapter<Image, AdapterImagePickerBinding,
        ImagePickerAdapter.AlbumPickerViewHolder>() {

    private var mImageClickListener: ((Image) -> Unit)? = null

    fun setImageClickListener(listener: ((Image) -> Unit)) {
        mImageClickListener = listener
    }

    override fun getLayout() = R.layout.adapter_image_picker

    override fun getViewHolder(binding: AdapterImagePickerBinding) =
        AlbumPickerViewHolder(binding)

    override fun onBindViewHolder(holder: AlbumPickerViewHolder, position: Int) {
        val image = getItem(position)
        holder.binding.image = image
    }

    inner class AlbumPickerViewHolder(val binding: AdapterImagePickerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageLyt.setOnClickListener {
                mImageClickListener?.invoke(it.tag as Image)
            }
        }

    }
}
