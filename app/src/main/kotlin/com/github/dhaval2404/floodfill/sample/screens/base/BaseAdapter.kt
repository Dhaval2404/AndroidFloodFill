package com.github.dhaval2404.floodfill.sample.screens.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic Base Adapter
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
abstract class BaseAdapter<T, VDB : ViewDataBinding, VH : RecyclerView.ViewHolder>(
    protected val itemList: MutableList<T> = ArrayList()
) : RecyclerView.Adapter<VH>() {

    constructor() : this(ArrayList())

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun getItemCount() = itemList.size

    open fun getItem(position: Int) = itemList[position]

    protected open fun getViewHolder(binding: VDB): VH {
        return object : RecyclerView.ViewHolder(binding.root) {} as VH
    }

    protected open fun getViewHolder(binding: VDB, viewType: Int): VH {
        return getViewHolder(binding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val viewDataBinding: VDB = DataBindingUtil.inflate(
            inflater, getLayout(),
            parent, false
        )
        return getViewHolder(viewDataBinding, viewType)
    }

    open fun refresh(list: List<T>) {
        itemList.clear()
        itemList.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }

    fun notifyItemChanged(vararg indexes:  Int) {
        indexes.forEach {
            notifyItemChanged(it)
        }
    }

}
