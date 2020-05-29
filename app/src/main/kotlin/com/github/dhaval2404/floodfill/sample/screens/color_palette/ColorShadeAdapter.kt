package com.github.dhaval2404.floodfill.sample.screens.color_palette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.databinding.AdapterColorShadeBinding
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class ColorShadeAdapter : BaseAdapter<Int, AdapterColorShadeBinding,
        ColorShadeAdapter.ColorPaletteViewHolder>() {

    private var _colorShadeLiveData = MutableLiveData<Int>()
    val colorShadeLiveData: LiveData<Int> = _colorShadeLiveData

    override fun getLayout() = R.layout.adapter_color_shade

    override fun getViewHolder(binding: AdapterColorShadeBinding) =
        ColorPaletteViewHolder(binding)

    override fun onBindViewHolder(holder: ColorPaletteViewHolder, position: Int) {
        val shade = getItem(position)
        holder.binding.shade = shade
        holder.binding.isChecked = _colorShadeLiveData.value == shade
    }

    fun setDefaultShade(color: Int) {
        _colorShadeLiveData.value = color
        notifyDataSetChanged()
    }

    inner class ColorPaletteViewHolder(val binding: AdapterColorShadeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.colorShade.setOnClickListener {
                val shade = it.tag as Int

                val newIndex = itemList.indexOf(shade)
                val oldIndex = itemList.indexOf(_colorShadeLiveData.value)

                _colorShadeLiveData.value = shade

                notifyItemChanged(newIndex, oldIndex)
            }
        }
    }
}