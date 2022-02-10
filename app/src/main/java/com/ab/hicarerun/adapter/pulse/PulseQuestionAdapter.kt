package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutPulseParentBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionList
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList

class PulseQuestionAdapter(val context: Context): RecyclerView.Adapter<PulseQuestionAdapter.MyHolder>(){

    val items = ArrayList<QuestionList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutPulseParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        val answersAdapter = PulseAnswerChildAdapter(context)
        holder.binding.recycleChild.config(answersAdapter)
        val sublistQuestionAdapter = PulseSublistQuestionAdapter(context)
        holder.binding.recycleSubChild.config(sublistQuestionAdapter)
        answersAdapter.addData(
            items[position].questionOption,
            items[position].questionId,
            items[position].questionType,
            items[position].answer,
            items[position].isDisabled)
        answersAdapter.setOnListPresentListener(object : PulseAnswerChildAdapter.OnSubListPresentListener{
            override fun sendAndNotify(position: Int, isSublistPresent: Boolean?, subList: List<SubQuestionList>?) {
                if (isSublistPresent == true && !subList.isNullOrEmpty()){
                    sublistQuestionAdapter.addData(subList)
                    holder.binding.recycleSubChild.visibility = View.VISIBLE
                }else{
                    holder.binding.recycleSubChild.visibility = View.GONE
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun RecyclerView.config(answerChildAdapter: PulseAnswerChildAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = answerChildAdapter
    }
    private fun RecyclerView.config(sublistQuestionAdapter: PulseSublistQuestionAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = sublistQuestionAdapter
    }


    fun addData(items: ArrayList<QuestionList>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutPulseParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(question: QuestionList){
            binding.txtQuest.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            binding.txtQuest.text = question.questionDisplayText
        }
    }
}