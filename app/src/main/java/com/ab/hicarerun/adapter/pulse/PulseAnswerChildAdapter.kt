package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutPulseChildBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionList
import com.ab.hicarerun.network.models.pulsemodel.QuestionOption
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList

class PulseAnswerChildAdapter(val context: Context) : RecyclerView.Adapter<PulseAnswerChildAdapter.MyHolder>() {

    var onSubListPresentListener: OnSubListPresentListener? = null
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
        val view = LayoutPulseChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])

        /*if (items[position].isSubQuestion == true){
            val sublistQuestionAdapter = PulseSublistQuestionAdapter(context)
            //holder.binding.recycleSubChild.config(sublistQuestionAdapter)
            sublistQuestionAdapter.addData(items[position].subQuestionList)
        }*/

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

        holder.binding.rbAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position].isSelected = true
            holder.binding.rbAnswers.isChecked = true
            for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false;
                    items[i].isSelected = false
                }
            }
            onSubListPresentListener?.sendAndNotify(
                selectedPos,
                items[position].isSubQuestion,
                items[position].subQuestionList
            )
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun RecyclerView.config(sublistQuestionAdapter: PulseSublistQuestionAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = sublistQuestionAdapter
    }

    fun addData(items: List<QuestionOption>?, questionId: Int?, type: String?, answer: String?, isDisabled: Boolean?){
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        this.questionId = questionId.toString()
        this.type = type.toString()
        this.answer = answer.toString()
        this.isDisabled = isDisabled == true
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutPulseChildBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(option: QuestionOption){
            binding.rbAnswers.text = option.optionDisplayText
            binding.chkAnswers.text = option.optionDisplayText
            binding.rbAnswers.isChecked = option.isSelected==true
            binding.chkAnswers.isChecked = option.isSelected==true
        }
    }

    fun setOnListPresentListener(onSubListPresentListener: OnSubListPresentListener){
        this.onSubListPresentListener = onSubListPresentListener
    }

    interface OnSubListPresentListener{
        fun sendAndNotify(position: Int, isSublistPresent: Boolean?, subList: List<SubQuestionList>?)
    }
}