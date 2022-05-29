package com.ekochkov.appfstr.view.adapters

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.data.entity.Image
import com.ekochkov.appfstr.utils.Base64Converter
import com.ekochkov.appfstr.view.viewHolders.ImageHolder

class ImageAdapter(private val onClickListener: ImageHolder.onClickListener): RecyclerView.Adapter<ImageHolder>() {

    val images = arrayListOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val image = images[position]
        val bitmap = Base64Converter.fromBase64ToBitmap(image.url!!)
        if (bitmap!=null) {
            holder.binding.image.setImageBitmap(bitmap)
        }
        holder.binding.closeImage.setOnClickListener {
            onClickListener.onDeleteImageClick(image)
        }
        holder.binding.descriptionEditText.editText?.setText(image.title)

        holder.binding.descriptionEditText.editText?.doAfterTextChanged {
            if (!it.isNullOrEmpty()) {
                val newImage = Image(it.toString(), image.url)
                if (newImage != image) {
                    onClickListener.onImageChanged(image, newImage)
                    images[position].title = it.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
