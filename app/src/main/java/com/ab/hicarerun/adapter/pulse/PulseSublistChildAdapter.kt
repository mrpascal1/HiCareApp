package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutSublistChildBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionOption

class PulseSublistChildAdapter(val context: Context): RecyclerView.Adapter<PulseSublistChildAdapter.MyHolder>(){

    val items = ArrayList<QuestionOption>()
    var questionId = ""
    var type = ""
    var answer = ""
    var isDisabled = false
    var givenAnswer = ""
    var selectedPos = -1
    var isSelected = false
    var dropdownArr: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutSublistChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        if (type.equals("Single Select", true)){
            holder.binding.rbAnswers.visibility = View.VISIBLE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
        }
        if (type.equals("Multi Select", true)){
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.VISIBLE
        }
        if (type.equals("DropdownSingleSelect", true)){
            holder.binding.spinnerLayout.visibility = View.VISIBLE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: List<QuestionOption>?, questionId: Int?, type: String?, answer: String?, isDisabled: Boolean?){
        this.items.clear()
        if (items != null){
            this.items.addAll(items)
        }
        this.questionId = questionId.toString()
        this.type = type.toString()
        this.answer = answer.toString()
        this.isDisabled = isDisabled == true
        notifyDataSetChanged()
    }
    class MyHolder(val binding: LayoutSublistChildBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(option: QuestionOption){
            binding.rbAnswers.text = option.optionDisplayText
            binding.chkAnswers.text = option.optionDisplayText
            binding.rbAnswers.isChecked = option.isSelected==true
            binding.chkAnswers.isChecked = option.isSelected==true
        }
    }
}