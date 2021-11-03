package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutQuestionParentAdapterBinding
import com.ab.hicarerun.handler.OnListItemClickHandler
import com.ab.hicarerun.network.models.TmsModel.QuestionsResponse

class TmsQuestionsParentAdapter(val context: Context, val items: ArrayList<String>) : RecyclerView.Adapter<TmsQuestionsParentAdapter.MyHolder>() {

    var onItemClickHandler: OnListItemClickHandler? = null
    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutQuestionParentAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        /*holder.itemView.setOnClickListener {
            selectedPos = position
            onItemClickHandler?.onItemClick(position)
            notifyDataSetChanged()
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(item: String){
        items.clear()
        items.add(item)
    }


    class MyHolder(val binding: LayoutQuestionParentAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: String){
            binding.txtQuest.text = item
        }
    }

    fun setOnClickHandler(onItemClickHandler: OnListItemClickHandler){
        this.onItemClickHandler = onItemClickHandler
    }
}