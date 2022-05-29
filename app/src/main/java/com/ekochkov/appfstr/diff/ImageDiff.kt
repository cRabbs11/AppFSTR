package com.ekochkov.appfstr.diff

import androidx.recyclerview.widget.DiffUtil
import com.ekochkov.appfstr.data.entity.Image

class ImageDiff(private val oldList: List<Image>, private val newList: List<Image>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition] == newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return (oldItem.title == newItem.title &&
                oldItem.url == newItem.url)
    }
}