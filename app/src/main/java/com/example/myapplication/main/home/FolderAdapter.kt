package com.example.myapplication.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemHomeFolderBinding
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.extension.disableMultipleClick

class FolderAdapter : ListAdapter<Folder, FolderAdapter.ItemVH>(FolderDiffCallback()) {
    var onItemCLick: (Folder) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            ItemHomeFolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(position)
    }

    inner class ItemVH(private val view: ItemHomeFolderBinding) :
        RecyclerView.ViewHolder(view.root) {
        init {
            view.root.disableMultipleClick {
                onItemCLick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(position: Int) {
            view.run {
                tvName.text = getItem(position).name
            }
        }
    }

    class FolderDiffCallback : DiffUtil.ItemCallback<Folder>() {
        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean =
            oldItem.name == newItem.name
    }
}
