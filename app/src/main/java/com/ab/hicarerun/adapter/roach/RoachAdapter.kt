package com.ab.hicarerun.adapter.roach

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.ItemRoachListBinding
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachList

class RoachAdapter(val context: Context): RecyclerView.Adapter<RoachAdapter.MyHolder>() {

    val items = ArrayList<RoachList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemRoachListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: List<RoachList>?){
        this.items.clear()
        if (items != null){
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    class MyHolder(val binding: ItemRoachListBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(data: RoachList){
            binding.nameTv.text = data.deviceDisplayName
            binding.locationTv.text = data.deployedLocation
            binding.createdOnTv.text = data.createdOnDisplay
        }
    }
}