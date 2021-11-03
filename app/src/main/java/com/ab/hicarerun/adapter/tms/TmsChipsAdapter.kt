package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ItemTmsChipsBinding

class TmsChipsAdapter(val context: Context, val items: ArrayList<String>) : RecyclerView.Adapter<TmsChipsAdapter.MyHolder>(){

    var onItemClickHandler: OnListItemClickHandler? = null
    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemTmsChipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        if (selectedPos == position) {
            holder.binding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary));
            holder.binding.lnrCategory.background = context.resources.getDrawable(R.drawable.green_round_border);
            holder.binding.categoryTv.setTextColor(context.resources.getColor(R.color.colorPrimary));
        } else {
            holder.binding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.greyclose));
            holder.binding.lnrCategory.background = context.resources.getDrawable(R.drawable.white_round_border);
            holder.binding.categoryTv.setTextColor(context.resources.getColor(R.color.greyclose));
        }
        holder.itemView.setOnClickListener {
            selectedPos = position
            onItemClickHandler?.onItemClick(position, items[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyHolder(val binding: ItemTmsChipsBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: String){
            binding.categoryTv.text = item
        }
    }

    fun setOnListItemClickHandler(onListItemClickHandler: OnListItemClickHandler){
        this.onItemClickHandler = onListItemClickHandler
    }

    interface OnListItemClickHandler {
        fun onItemClick(position: Int, category: String)
    }
}