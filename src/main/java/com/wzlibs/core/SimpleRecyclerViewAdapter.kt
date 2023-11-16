package com.wzlibs.core

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleRecyclerViewAdapter<T, V : ViewBinding>(
    var mDataList: MutableList<T> = mutableListOf(),
    var isItemClick: Boolean = false
) : RecyclerView.Adapter<BaseViewHolder<T, V>>() {
    var listPrevious = arrayListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, V> {
        return BaseViewHolder(providesItemViewBinding(parent, viewType))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T, V>, position: Int) {
        bindData(holder.binding, mDataList[position], position, holder.binding.root.context)
        if (isItemClick) {
            holder.binding.root.setOnClickListener {
                onItemSelectListener(mDataList[position], holder.binding, position)
            }
        }
    }

    abstract fun providesItemViewBinding(parent: ViewGroup, viewType: Int): V
    fun add(itemList: List<T>) {
        val size = this.mDataList.size
        this.mDataList.addAll(itemList)
        val sizeNew = this.mDataList.size
        notifyItemRangeChanged(size, sizeNew)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addAt(position: Int, item: T) {
        mDataList.add(position, item)
        notifyDataSetChanged()
    }

    fun addAt(position: Int, itemList: List<T>) {
        val size = this.mDataList.size
        this.mDataList.addAll(position, itemList)
        val sizeNew = this.mDataList.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun setItemAt(position: Int, item: T) {
        mDataList[position] = item
        notifyItemChanged(position)
    }

    fun getItemAt(position: Int): T {
        return mDataList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun set(dataList: List<T>) {
        val clone: List<T> = ArrayList(dataList)
        listPrevious = ArrayList(mDataList)
        mDataList.clear()
        mDataList.addAll(clone)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeAt(position: Int) {
        mDataList.removeAt(position)
        notifyDataSetChanged()
    }

    fun removeAtPosition(position: Int) {
        if (position > -1 && position < mDataList.size) {
            mDataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    fun backPreviousList(): Boolean {
        if (listPrevious.size > 0) {
            set(listPrevious)
            listPrevious = arrayListOf()
            return true
        }
        return false
    }

    abstract fun bindData(binding: V, data: T, position: Int, context: Context)

    override fun getItemCount(): Int = mDataList.size

    var onItemSelectListener: (T, V, Int) -> Unit = { t, v, position -> }
}