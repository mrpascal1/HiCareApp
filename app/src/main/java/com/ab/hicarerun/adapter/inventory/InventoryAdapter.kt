package com.ab.hicarerun.adapter.inventory

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ItemInventoryListBinding
import com.ab.hicarerun.network.models.inventorymodel.inventorylistmodel.Data

class InventoryAdapter(val context: Context) : RecyclerView.Adapter<InventoryAdapter.MyHolder>(){

    var historyClickListener: HistoryClickListener? = null
    var items = ArrayList<Data>()
    var isFromTask = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemInventoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position], isFromTask)
        historyClickListener?.onClick(items[position].item_Code.toString())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(item: List<Data>?, isFromTask: Boolean){
        this.isFromTask = isFromTask
        items.clear()
        items.addAll(item!!)
    }

    class MyHolder(val binding: ItemInventoryListBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(data: Data, isFromTask: Boolean){
            binding.inventoryCodeTv.setTypeface(null, Typeface.BOLD)
            binding.statusTv.setTypeface(null, Typeface.BOLD)
            binding.inventoryCodeTv.text = data.inventory_Code.toString()
            binding.itemCodeTv.text = data.item_Code.toString()
            binding.regionTv.text = data.region_Name.toString()
            binding.manufacturingOnTv.text = data.manufacturing_Date__Date.toString()
            binding.expiringOnTv.text = data.expiry_Date__Date.toString()
            if (data.status.equals("Free", true)){
                binding.statusTv.setTextColor(ContextCompat.getColor(context, R.color.red))
            }else if (data.status.equals("In use", true)){
                binding.statusTv.setTextColor(ContextCompat.getColor(context, R.color.md_yellow_800))
            }
            binding.statusTv.text = data.status.toString()
            if (isFromTask){
                binding.historyBtn.visibility = View.GONE
            }else{
                binding.historyBtn.visibility = View.VISIBLE
            }
        }
    }

    interface HistoryClickListener{
        fun onClick(itemCode: String)
    }
}