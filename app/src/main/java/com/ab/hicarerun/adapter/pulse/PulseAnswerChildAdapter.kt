package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.LayoutPulseChildBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionOption
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList
import com.ab.hicarerun.utils.AppUtils

class PulseAnswerChildAdapter(val context: Context) : RecyclerView.Adapter<PulseAnswerChildAdapter.MyHolder>() {

    var onTextChangedListener: OnTextChangedListener? = null
    var onSubListPresentListener: OnSubListPresentListener? = null
    val items = ArrayList<QuestionOption?>()
    var questionId = ""
    var type = ""
    var answer = ""
    var questionTitle = ""
    var isDisabled = false
    var givenAnswer = ""
    var selectedPos = -1
    var isSelected = false
    var rating = -1
    var dropdownArr: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutPulseChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        Log.d("TAG", "Current ${AppUtils.pulseRating}")
        if (AppUtils.pulseRating != -1){
            selectEmoji(holder)
        }

        if (type.equals("Single Select", true)){
            holder.binding.rbAnswers.visibility = View.VISIBLE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
        }
        if (type.equals("Multi Select", true)){
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.VISIBLE
        }
        if (type.equals("DropdownSingleSelect", true)){
            holder.binding.spinnerLayout.visibility = View.VISIBLE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.ratingEmoji.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            if (questionTitle.equals("How happy is client with HiCare services", true)){
                holder.binding.spinnerLayout.visibility = View.GONE
                holder.binding.ratingEmoji.visibility = View.VISIBLE
                AppUtils.pulseRatingQID = questionId.toInt()
                if (answer.isNotEmpty()){
                    selectEmoji(holder)
                }
            }else{
                holder.binding.spinnerLayout.visibility = View.VISIBLE
                holder.binding.ratingEmoji.visibility = View.GONE
            }
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

        holder.binding.veryBadLayout.onClick(holder)
        holder.binding.badLayout.onClick(holder)
        holder.binding.averageLayout.onClick(holder)
        holder.binding.goodLayout.onClick(holder)
        holder.binding.veryGoodLayout.onClick(holder)

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
            onSubListPresentListener?.sendAndNotify(
                selectedPos,
                questionId,
                items[position]?.isSubQuestion,
                items[position]?.subQuestionList
            )
            onTextChangedListener?.onOptionChange(position, items[position]?.optionDisplayText.toString(), questionId.toInt(), "radio")
            notifyDataSetChanged()
        }
        holder.binding.chkAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = holder.binding.chkAnswers.isChecked
            onTextChangedListener?.onCheckboxClicked(position, holder.binding.chkAnswers.isChecked, items[position]?.optionDisplayText.toString(), questionId.toInt(), "checkbox")
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
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun LinearLayout.onClick(holder: MyHolder){
        setOnClickListener {
            set(holder)
        }
    }

    fun LinearLayout.set(holder: MyHolder){
        when {
            this == holder.binding.veryBadLayout -> {
                AppUtils.pulseRating = 1
            }
            this == holder.binding.badLayout -> {
                AppUtils.pulseRating = 2
            }
            this == holder.binding.averageLayout -> {
                AppUtils.pulseRating = 3
            }
            this == holder.binding.goodLayout -> {
                AppUtils.pulseRating = 4
            }
            this == holder.binding.veryGoodLayout -> {
                AppUtils.pulseRating = 5
            }
        }
        removeAllBgs(holder)
        setBackgroundResource(R.drawable.bg_green_border)
        onTextChangedListener?.onOptionChange(0, AppUtils.pulseRating.toString(), AppUtils.pulseRatingQID, "radio")
        Log.d("TAG", "Selected ${AppUtils.pulseRating}")
    }

    private fun selectEmoji(holder: MyHolder){
        Log.d("TAG", "Inside Select")
        Log.d("TAG", "${AppUtils.pulseRating}")
        when (AppUtils.pulseRating) {
            1 -> {
                holder.binding.veryBadLayout.set(holder)
            }
            2 -> {
                holder.binding.badLayout.set(holder)
            }
            3 -> {
                holder.binding.averageLayout.set(holder)
            }
            4 -> {
                holder.binding.goodLayout.set(holder)
            }
            5 -> {
                holder.binding.veryGoodLayout.set(holder)
            }
        }
    }

    fun removeAllBgs(holder: MyHolder){
        holder.binding.veryBadLayout.removeBg()
        holder.binding.badLayout.removeBg()
        holder.binding.averageLayout.removeBg()
        holder.binding.goodLayout.removeBg()
        holder.binding.veryGoodLayout.removeBg()
    }
    fun LinearLayout.removeBg(){
        setBackgroundResource(0)
    }

    fun addData(items: List<QuestionOption>?, questionTitle: String?, questionId: Int?, type: String?, answer: String?, isDisabled: Boolean?, questionStrOption: ArrayList<String>?){
        this.items.clear()
        if (items != null && items.isNotEmpty()){
            this.items.addAll(items)
        }else{
            this.items.add(null)
        }
        this.questionTitle = questionTitle.toString()
        this.questionId = questionId.toString()
        this.type = type.toString()
        this.answer = answer.toString()
        this.isDisabled = isDisabled == true
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

    class MyHolder(val binding: LayoutPulseChildBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(option: QuestionOption?){
            binding.rbAnswers.text = option?.optionDisplayText
            binding.chkAnswers.text = option?.optionDisplayText
            binding.rbAnswers.isChecked = option?.isSelected==true
            binding.chkAnswers.isChecked = option?.isSelected==true
        }
    }

    fun setOnListPresentListener(onSubListPresentListener: OnSubListPresentListener){
        this.onSubListPresentListener = onSubListPresentListener
    }

    interface OnSubListPresentListener{
        fun sendAndNotify(p: Int, qId: String, isSublistPresent: Boolean?, subList: List<SubQuestionList>?)
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