package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.tms.TmsAnswersChildAdapter
import com.ab.hicarerun.databinding.LayoutSublistChildBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionOption
import com.ab.hicarerun.utils.AppUtils
import com.google.android.material.chip.Chip

class PulseSublistChildAdapter(val context: Context): RecyclerView.Adapter<PulseSublistChildAdapter.MyHolder>(){

    var onTextChangedListener: OnTextChangedListener? = null
    val items = ArrayList<QuestionOption?>()
    var questionId = ""
    var type = ""
    var answer = ""
    var isDisabled = false
    var givenAnswer = ""
    var selectedPos = -1
    var isSelected = false
    var dropdownArr: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null
    val states = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked)
    )
    val colors = intArrayOf(
        Color.parseColor("#2BB77A"),
        // chip unchecked color
        Color.parseColor("#E0E0E0")
    )
    val colorList = ColorStateList(states, colors)

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
            holder.binding.chipLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
        }
        if (type.equals("Multi Select", true)){
            holder.binding.chkAnswers.visibility = View.VISIBLE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chipLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
        }
        if (type.equals("DropdownSingleSelect", true)){
            holder.binding.spinnerLayout.visibility = View.VISIBLE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.chipLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE

            arrayAdapter?.setDropDownViewResource(R.layout.spinner_popup)
            holder.binding.spnType.adapter = arrayAdapter
            if (answer != "") {
                for (i in 0 until dropdownArr.size){
                    if (dropdownArr[i] == answer){
                        holder.binding.spnType.setSelection(i)
                    }
                }
            }
        }
        if (type.equals("ChipMultiSelect", true)){
            holder.binding.chipLayout.visibility = View.VISIBLE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
            dropdownArr.forEach {
                Log.d("Chip", it)
                val chip = Chip(holder.binding.chipGroup.context)
                chip.text = it
                chip.isClickable = true
                chip.isCheckable = true
                chip.isCheckedIconVisible = false
                chip.chipBackgroundColor = colorList
                if (it == answer){
                    chip.isChecked = true
                }
                holder.binding.chipGroup.addView(chip)
                chip.setOnClickListener {
                    Log.d("TAG: ", "Position $position and ID ${items[position]}")
                    selectedPos = position
                    //items[position]?.isSelected = chip.isChecked
                    onTextChangedListener?.onCheckboxClicked(position, chip.isChecked, items[position]?.optionDisplayText.toString(), questionId.toInt(), "checkbox")
                    notifyDataSetChanged()
                }
            }
        }
        if (type.equals("SingleRating", true)){
            holder.binding.ratingEmoji.visibility = View.VISIBLE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.chipLayout.visibility = View.GONE
            AppUtils.pulseRatingQID = questionId.toInt()
            if (answer.isNotEmpty()){
                //selectEmoji(holder)
            }
        }

        holder.binding.rbAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = true
            holder.binding.rbAnswers.isChecked = true
            for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false
                    items[i]?.isSelected = false
                }
            }
            onTextChangedListener?.onOptionChange(position, items[position]?.optionDisplayText.toString(), questionId.toInt(), "radio")
            notifyDataSetChanged()
        }

        holder.binding.chkAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = holder.binding.chkAnswers.isChecked
            onTextChangedListener?.onCheckboxClicked(position, holder.binding.chkAnswers.isChecked, items[position]?.optionDisplayText.toString(), questionId.toInt(), "checkbox")
            /*for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false;
                    items[i].isSelected = false
                }
            }*/
            notifyDataSetChanged()
        }

        holder.binding.spnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = holder.binding.spnType.selectedItem.toString()
                if (selected != "Select Option"){
                    selectedPos = position
                    items[position]?.isSelected = true
                    for (i in 0 until items.size){
                        if (selectedPos != i) {
                            items[i]?.isSelected = false
                        }
                    }
                    onTextChangedListener?.onTextChange(position, selected, questionId.toInt())
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: List<QuestionOption>?, questionId: Int?, type: String?, answer: String?, isDisabled: Boolean?, questionStrOption: ArrayList<String>?){
        this.items.clear()
        if (items != null && items.isNotEmpty()){
            this.items.addAll(items)
        }else{
            this.items.add(null)
        }
        this.questionId = questionId.toString()
        this.type = type.toString()
        this.answer = answer.toString()
        this.isDisabled = isDisabled == true
        if (type.equals("ChipMultiSelect", true)){
            dropdownArr.clear()
            if (!questionStrOption.isNullOrEmpty()){
                dropdownArr.addAll(questionStrOption)
            }
        }
        if (type.equals("DropdownSingleSelect", true)){
            dropdownArr.clear()
            dropdownArr.add(0, "Select Option")
            if (!questionStrOption.isNullOrEmpty()){
                dropdownArr.addAll(questionStrOption)
            }
            Log.d("TAG", "Drop down $dropdownArr")
            arrayAdapter = object : ArrayAdapter<String>(context, R.layout.spinner_layout_new, dropdownArr){
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0){
                        tv.setTextColor(Color.GRAY)
                    }else{
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }
        }
        //notifyDataSetChanged()
    }
    class MyHolder(val binding: LayoutSublistChildBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(option: QuestionOption?){
            binding.rbAnswers.text = option?.optionDisplayText
            binding.chkAnswers.text = option?.optionDisplayText
            binding.rbAnswers.isChecked = option?.isSelected==true
            binding.chkAnswers.isChecked = option?.isSelected==true
        }
    }

    fun setOnChildTextChangedListener(onTextChangedListener: OnTextChangedListener){
        this.onTextChangedListener = onTextChangedListener
    }

    interface OnTextChangedListener{
        fun onTextChange(childPosition: Int, str: String, questionId: Int)
        fun onOptionChange(childPosition: Int, str: String, questionId: Int, type: String)
        fun onCheckboxClicked(childPosition: Int, isChecked: Boolean, str: String, questionId: Int, type: String)
    }
}