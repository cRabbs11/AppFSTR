package com.ekochkov.appfstr.view.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.appfstr.databinding.ItemImageBinding

class ImageHolder(val view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemImageBinding.bind(view)
}