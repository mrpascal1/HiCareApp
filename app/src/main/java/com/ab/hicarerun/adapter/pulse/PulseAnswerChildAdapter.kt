package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.LayoutPulseChildBinding
import com.ab.hicarerun.fragments.tms.TmsUtils
import com.ab.hicarerun.network.models.pulsemodel.QuestionList
import com.ab.hicarerun.network.models.pulsemodel.QuestionOption
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList

class PulseAnswerChildAdapter(val context: Context) : RecyclerView.Adapter<PulseAnswerChildAdapter.MyHolder>() {

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
    var prevEmojiSelected = -1
    var dropdownArr: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutPulseChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])

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
            }else{
                holder.binding.spinnerLayout.visibility = View.VISIBLE
                holder.binding.ratingEmoji.visibility = View.GONE
            }
        }

        arrayAdapter?.setDropDownViewResource(R.layout.spinner_popup)
        holder.binding.spnType.adapter = arrayAdapter
        if (givenAnswer != "") {
            for (i in 0 until dropdownArr.size){
                if (dropdownArr[i] == givenAnswer){
                    holder.binding.spnType.setSelection(i)
                }
            }
        }

        holder.binding.veryBad.onClick(position, holder)
        holder.binding.badLayout.onClick(position, holder)
        holder.binding.averageLayout.onClick(position, holder)
        holder.binding.goodLayout.onClick(position, holder)
        holder.binding.veryGoodLayout.onClick(position, holder)

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
                items[position]?.isSubQuestion,
                items[position]?.subQuestionList
            )
            notifyDataSetChanged()
        }
        holder.binding.chkAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = holder.binding.chkAnswers.isChecked
            //onTextChangedListener?.onCheckboxClicked(position, holder.binding.chkAnswers.isChecked, items[position]?.optionDisplayText.toString(), questionId, "checkbox")
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
                    //onTextChangedListener?.onTextChange(position, selected, questionId)
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

    fun LinearLayout.onClick(position: Int, holder: MyHolder){
        setOnClickListener {
            removeAllBgs(holder)
            setBackgroundResource(R.drawable.bg_green_border)
            prevEmojiSelected = position
        }
    }

    fun removeAllBgs(holder: MyHolder){
        holder.binding.veryBad.removeBg()
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
        notifyDataSetChanged()
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
        fun sendAndNotify(position: Int, isSublistPresent: Boolean?, subList: List<SubQuestionList>?)
    }
}