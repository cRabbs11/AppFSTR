package com.ekochkov.appfstr.view.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.appfstr.data.entity.Image
import com.ekochkov.appfstr.databinding.ItemImageBinding

class ImageHolder(val view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemImageBinding.bind(view)

    interface onClickListener {
        fun onDeleteImageClick(image: Image)
        fun onImageChanged(oldImage: Image, newImage: Image)
    }
}