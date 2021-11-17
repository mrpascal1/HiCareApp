package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutTmsChildAdapterBinding
import com.ab.hicarerun.network.models.TmsModel.Option
import com.ab.hicarerun.network.models.TmsModel.QuestionOption
import java.lang.Exception

class TmsAnswersChildAdapter(val context: Context) : RecyclerView.Adapter<TmsAnswersChildAdapter.MyHolder>() {

    var onTextChangedListener: OnTextChangedListener? = null
    var items: ArrayList<QuestionOption?> = ArrayList()
    var selectedPos = -1
    var isSelected = false
    var ansType = ""
    var questionId = -1
    var qAnswer = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutTmsChildAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.d("TAG", "ans $ansType")
        holder.bindItems(items[position])

        Log.d("TAG", "items $items")
        if (ansType.equals("single select", true)){
            holder.binding.rbAnswers.visibility = View.VISIBLE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
        }
        if (ansType.equals("multi select", true)){
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.VISIBLE
        }
        if (ansType.equals("numbertext", true)){
            holder.binding.numberEt.setText(qAnswer)
            if (holder.binding.numberEt.text.toString().trim() == ""){
                holder.binding.numberEt.setText("0")
            }
            holder.binding.numberLayout.visibility = View.VISIBLE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
        }

        holder.binding.rbAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = true
            holder.binding.rbAnswers.isChecked = true
            for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false;
                    items[i]?.isSelected = false
                }
            }
            onTextChangedListener?.onOptionChange(position, items[position]?.optionText.toString(), questionId)
            notifyDataSetChanged()
        }

        holder.binding.chkAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = holder.binding.chkAnswers.isChecked
            onTextChangedListener?.onOptionChange(position, items[position]?.optionText.toString(), questionId)
            /*for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false;
                    items[i].isSelected = false
                }
            }*/
            notifyDataSetChanged()
        }

        holder.binding.numberEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                selectedPos = position
                onTextChangedListener?.onTextChange(position, p0.toString(), questionId)
            }
        })

        holder.binding.addBtn.setOnClickListener {
            addClick(holder, position)
        }

        holder.binding.minusBtn.setOnClickListener {
            minusClick(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun addClick(holder: MyHolder, position: Int){
        try {
            if (holder.binding.numberEt.text.toString().toInt() < 9999) {
                holder.binding.numberEt.setText("${holder.binding.numberEt.text.toString().toInt() + 1}")
            }else{
                Toast.makeText(context, "Maximum value", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){ }
    }

    private fun minusClick(holder: MyHolder, position: Int) {
        try {
            if (holder.binding.numberEt.text.toString().toInt() > 0) {
                holder.binding.numberEt.setText("${holder.binding.numberEt.text.toString().toInt() - 1}")
            }else{
                Toast.makeText(context, "Minimum value", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) { }
    }

    fun addData(options: List<QuestionOption>?, answer: String?, type: String?, id: Int?){
        items.clear()
        if (options != null) {
            items.addAll(options)
        }else{
            items.add(null)
        }
        qAnswer = answer.toString()
        ansType = type.toString()
        questionId = id.toString().toInt()
        Log.d("TAG", "Ans Type $ansType")
    }
    fun addNumberText(answer: String?, type: String?, id: Int?){
        items.add(null)
        qAnswer = answer.toString()
        ansType = type.toString()
        questionId = id.toString().toInt()
    }

    class MyHolder(val binding: LayoutTmsChildAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: QuestionOption?){
            binding.chkAnswers.visibility = View.GONE
            binding.rbAnswers.text = item?.optionText
            binding.chkAnswers.text = item?.optionText
            binding.rbAnswers.isChecked = item?.isSelected==true
            binding.chkAnswers.isChecked = item?.isSelected==true
        }
    }

    fun setOnChildTextChangedListener(onTextChangedListener: OnTextChangedListener){
        this.onTextChangedListener = onTextChangedListener
    }

    interface OnTextChangedListener{
        fun onTextChange(childPosition: Int, str: String, questionId: Int)
        fun onOptionChange(childPosition: Int, str: String, questionId: Int)
    }
}