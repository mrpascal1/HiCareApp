package com.ab.hicarerun.adapter.roach

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.ItemRoachListBinding

class RoachAdapter(val context: Context): RecyclerView.Adapter<RoachAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemRoachListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return 0
    }

    class MyHolder(val binding: ItemRoachListBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(){

        }
    }
}