package com.github.dhaval2404.floodfill.sample.screens.color_palette

import android.graphics.Color
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

    private var mShade: Int = Color.parseColor("#5c6bc0")
    private var mColorShadeListener: ((Int)->Unit)? = null

    fun setColorShadeListener(listener: (Int)->Unit){
        this.mColorShadeListener = listener
    }

    override fun getLayout() = R.layout.adapter_color_shade

    override fun getViewHolder(binding: AdapterColorShadeBinding) =
        ColorPaletteViewHolder(binding)

    override fun onBindViewHolder(holder: ColorPaletteViewHolder, position: Int) {
        val shade = getItem(position)
        holder.binding.shade = shade
        holder.binding.isChecked = mShade == shade
    }

    inner class ColorPaletteViewHolder(val binding: AdapterColorShadeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.colorShade.setOnClickListener {
                val shade = it.tag as Int

                val newIndex = itemList.indexOf(shade)
                val oldIndex = itemList.indexOf(mShade)

                mShade = shade

                notifyItemChanged(newIndex)
                notifyItemChanged(oldIndex)

                mColorShadeListener?.invoke(shade)
            }
        }
    }
}