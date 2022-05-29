package com.ekochkov.appfstr.view.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.databinding.ItemMountainBinding


class MountainHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemMountainBinding.bind(view)

    interface OnMountainClickListener {
        fun onCheckBoxClick(mountain: Mountain, isChecked: Boolean)
        fun onPhotoBtnClick(mountain: Mountain)
        fun onEditBtnClick(mountain: Mountain)
    }
}