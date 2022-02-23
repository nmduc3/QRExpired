package com.example.myapplication.main.folder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemFoodBinding
import com.example.myapplication.main.data.database.model.FoodInfo

class FoodAdapter : ListAdapter<FoodInfo, FoodAdapter.ItemVH>(FoodInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            ItemFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(position)
    }

    inner class ItemVH(private val view: ItemFoodBinding) :
        RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            view.run {
                getItem(position).let {
                    tvName.text = it.name
                    tvId.text = "ID: ${it.foodId}"
                    tvDateStart.text = it.dateStart
                    tvDateExpired.text = it.getDateExpired()
                }
            }
        }
    }

    class FoodInfoDiffCallback : DiffUtil.ItemCallback<FoodInfo>() {
        override fun areItemsTheSame(oldItem: FoodInfo, newItem: FoodInfo): Boolean =
            oldItem.foodId == newItem.foodId

        override fun areContentsTheSame(oldItem: FoodInfo, newItem: FoodInfo): Boolean =
            oldItem.name == newItem.name &&
                    oldItem.dateStart == newItem.dateStart
    }
}
