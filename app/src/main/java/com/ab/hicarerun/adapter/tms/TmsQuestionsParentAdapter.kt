package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutQuestionParentAdapterBinding
import com.ab.hicarerun.databinding.LayoutTmsParentAdapterBinding
import com.ab.hicarerun.handler.OnListItemClickHandler
import com.ab.hicarerun.network.models.TmsModel.*

class TmsQuestionsParentAdapter(val context: Context) : RecyclerView.Adapter<TmsQuestionsParentAdapter.MyHolder>() {

    var items: ArrayList<QuestionList> = ArrayList()
    var optionList: ArrayList<Option> = ArrayList()
    var onItemClickHandler: OnListItemClickHandler? = null
    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutTmsParentAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(context, view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])


        val answersChildAdapter = TmsAnswersChildAdapter(context)
        answersChildAdapter.addData(items[position].questionOption, items[position].questionType)

        holder.binding.recycleChild.layoutManager = LinearLayoutManager(context)
        holder.binding.recycleChild.setHasFixedSize(true)
        holder.binding.recycleChild.clipToPadding = false
        holder.binding.recycleChild.adapter = answersChildAdapter


        /*holder.itemView.setOnClickListener {
            selectedPos = position
            onItemClickHandler?.onItemClick(position)
            notifyDataSetChanged()
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(item: List<QuestionList>){
        items.clear()
        if (item != null) {
            items.addAll(item)
        }
    }


    class MyHolder(val context: Context, val binding: LayoutTmsParentAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: QuestionList){
            binding.txtQuest.text = item.questionText
        }
    }

    fun setOnClickHandler(onItemClickHandler: OnListItemClickHandler){
        this.onItemClickHandler = onItemClickHandler
    }
}