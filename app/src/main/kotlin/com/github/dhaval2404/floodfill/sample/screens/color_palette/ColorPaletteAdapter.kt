package com.github.dhaval2404.floodfill.sample.screens.color_palette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.data.model.AppColor
import com.github.dhaval2404.floodfill.sample.databinding.AdapterColorPaletteBinding
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class ColorPaletteAdapter : BaseAdapter<AppColor, AdapterColorPaletteBinding,
        ColorPaletteAdapter.ColorPaletteViewHolder>() {

    private var _colorPaletteLiveData = MutableLiveData<AppColor>()
    val colorPaletteLiveData: LiveData<AppColor> = _colorPaletteLiveData

    override fun getLayout() = R.layout.adapter_color_palette

    override fun getViewHolder(binding: AdapterColorPaletteBinding) =
        ColorPaletteViewHolder(binding)

    override fun onBindViewHolder(holder: ColorPaletteViewHolder, position: Int) {
        val color = getItem(position)
        holder.binding.color = color
        holder.binding.isChecked = _colorPaletteLiveData.value?.palette == color.palette
    }

    fun setDefaultPalette(palette: String) {
        itemList.firstOrNull { it.palette == palette }?.let {
            _colorPaletteLiveData.value = it
        }
    }

    inner class ColorPaletteViewHolder(val binding: AdapterColorPaletteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.colorPalette.setOnClickListener {
                val color = it.tag as AppColor

                val newIndex = itemList.indexOf(color)
                val oldIndex = itemList.indexOf(_colorPaletteLiveData.value)

                _colorPaletteLiveData.value = color

                notifyItemChanged(newIndex, oldIndex)
            }
        }
    }
}
