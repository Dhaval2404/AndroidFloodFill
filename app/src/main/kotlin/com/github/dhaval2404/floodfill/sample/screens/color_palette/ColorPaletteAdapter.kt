package com.github.dhaval2404.floodfill.sample.screens.color_palette

import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.databinding.AdapterColorPaletteBinding
import com.github.dhaval2404.floodfill.sample.model.AppColor
import com.github.dhaval2404.floodfill.sample.screens.base.BaseAdapter

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class ColorPaletteAdapter : BaseAdapter<AppColor, AdapterColorPaletteBinding,
        ColorPaletteAdapter.ColorPaletteViewHolder>() {

    private var mColor: AppColor? = null
    private var mColorPaletteListener: ((AppColor)->Unit)? = null

    fun setColorPaletteListener(listener: (AppColor)->Unit){
        this.mColorPaletteListener = listener
    }

    override fun getLayout() = R.layout.adapter_color_palette

    override fun getViewHolder(binding: AdapterColorPaletteBinding) =
        ColorPaletteViewHolder(binding)

    override fun onBindViewHolder(holder: ColorPaletteViewHolder, position: Int) {
        val color = getItem(position)
        holder.binding.color = color
        holder.binding.isChecked = mColor == color
    }

    override fun refresh(list: List<AppColor>) {
        if (list.isNotEmpty()) {
            mColor = list.first { it.palette == "#5c6bc0" }
            mColorPaletteListener?.invoke(mColor!!)
        }
        super.refresh(list)
    }

    inner class ColorPaletteViewHolder(val binding: AdapterColorPaletteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.colorPalette.setOnClickListener {
                val color = it.tag as AppColor

                val newIndex = itemList.indexOf(color)
                val oldIndex = itemList.indexOf(mColor)

                mColor = color

                notifyItemChanged(newIndex)
                notifyItemChanged(oldIndex)

                mColorPaletteListener?.invoke(color)
            }
        }
    }
}
