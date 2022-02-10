package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutPulseParentBinding
import com.ab.hicarerun.databinding.LayoutSublistParentBinding
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList

class PulseSublistQuestionAdapter(val context: Context): RecyclerView.Adapter<PulseSublistQuestionAdapter.MyHolder>(){

    var items = ArrayList<SubQuestionList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutSublistParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        val pulseSublistChildAdapter = PulseSublistChildAdapter(context)
        holder.binding.recycleChild.config(pulseSublistChildAdapter)
        pulseSublistChildAdapter.addData(
            items[position].questionOption,
            items[position].questionId,
            items[position].questionType,
            items[position].answer,
            items[position].isDisabled)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun RecyclerView.config(sublistChildAdapter: PulseSublistChildAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = sublistChildAdapter
    }

    fun addData(items: List<SubQuestionList>?){
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutSublistParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: SubQuestionList){
            binding.txtQuest.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            binding.txtQuest.text = item.questionDisplayText
        }
    }
}