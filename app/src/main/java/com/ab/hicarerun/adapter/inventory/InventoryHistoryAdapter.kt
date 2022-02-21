package com.ab.hicarerun.adapter.inventory

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.ItemInventoryHistoryBinding
import com.ab.hicarerun.network.models.inventorymodel.historymodel.InventoryHistoryData

class InventoryHistoryAdapter(val context: Context) : RecyclerView.Adapter<InventoryHistoryAdapter.MyHolder>(){

    val items = ArrayList<InventoryHistoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemInventoryHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: ArrayList<InventoryHistoryData>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: ItemInventoryHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(data: InventoryHistoryData){
            binding.inventoryCodeTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            binding.inventoryCodeTv.text = data.inventory_id
            binding.bucketNameTv.text = data.bucket_Name
            binding.createdByTv.text = data.created_By
            binding.assignedDateTv.text = data.assigned_Date_Formated
            binding.assignTypeTv.text = data.assigned_Type
            binding.assignedToTv.text = data.assigned_To
        }
    }
}