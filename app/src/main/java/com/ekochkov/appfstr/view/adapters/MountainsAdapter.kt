package com.ekochkov.appfstr.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.databinding.MountainEditViewBinding
import com.ekochkov.appfstr.databinding.MountainStatusViewBinding
import com.ekochkov.appfstr.utils.DateConverter
import com.ekochkov.appfstr.view.viewHolders.MountainHolder

class MountainsAdapter(private val clickListener: MountainHolder.OnMountainClickListener) : RecyclerView.Adapter<MountainHolder>() {

    var mountainList = arrayListOf<Mountain>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MountainHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_mountain, parent, false)
        return MountainHolder(view)
    }

    override fun onBindViewHolder(holder: MountainHolder, position: Int) {
        val mountain = mountainList[position]
        holder.binding.date.text = DateConverter.fromLongToText(mountain.addTime!!.toLong(), DateConverter.FORMAT_DD_MM_YYYY)
        holder.binding.mountainName.text = mountain.title
        holder.binding.checkBox.setOnClickListener {
            clickListener.onCheckBoxClick(mountain, holder.binding.checkBox.isChecked)
        }

        val editBinding = MountainEditViewBinding.bind(holder.binding.editLayout.mountainEditLayout)
        val statusBinding = MountainStatusViewBinding.bind(holder.binding.statusLayout.mountainStatusLayout)

        if (mountain.id==-1) {
            editBinding.root.visibility = View.VISIBLE
            statusBinding.root.visibility = View.GONE
        } else {
            holder.binding.checkBox.visibility = View.INVISIBLE
            editBinding.root.visibility = View.GONE
            statusBinding.root.visibility = View.VISIBLE
            statusBinding.statusDate.text = DateConverter.fromLongToText(mountain.addTime!!.toLong(), DateConverter.FORMAT_DD_MM_YYYY)
            when(mountain.type) {
                Mountain.TYPE_PASS -> {
                    statusBinding.statusIcon.setImageResource(R.drawable.icon_on_sended)
                    statusBinding.statusText.text = holder.itemView.context.getString(R.string.sended)
                }
                Mountain.TYPE_ACCEPT -> {
                    statusBinding.statusIcon.setImageResource(R.drawable.icon_accepted)
                    statusBinding.statusText.text = holder.itemView.context.getString(R.string.accepted)
                }
                Mountain.TYPE_DECLINE -> {
                    statusBinding.statusIcon.setImageResource(R.drawable.icon_decline)
                    statusBinding.statusText.text = holder.itemView.context.getString(R.string.declined)
                }
                Mountain.TYPE_PUBLISHED -> {
                    statusBinding.statusIcon.setImageResource(R.drawable.icon_published)
                    statusBinding.statusText.text = holder.itemView.context.getString(R.string.published)
                }
            }
        }

        editBinding.editMountainBtn.setOnClickListener {
            clickListener.onEditBtnClick(mountain)
        }
    }

    override fun getItemCount(): Int {
        return mountainList.size
    }
}