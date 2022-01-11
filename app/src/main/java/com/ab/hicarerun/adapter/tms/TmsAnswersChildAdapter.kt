package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.LayoutTmsChildAdapterBinding
import com.ab.hicarerun.network.models.tmsmodel.QuestionOption
import com.ab.hicarerun.utils.AppUtils
import java.lang.Exception

class TmsAnswersChildAdapter(val context: Context, private val isServiceDeliverySheet: Boolean) : RecyclerView.Adapter<TmsAnswersChildAdapter.MyHolder>() {

    var onTextChangedListener: OnTextChangedListener? = null
    var items: ArrayList<QuestionOption?> = ArrayList()
    var selectedPos = -1
    var isSelected = false
    var ansType = ""
    var questionId = -1
    var qAnswer = ""
    var givenAnswer = ""
    var dropdownArr: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutTmsChildAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.d("TAG", "ans $ansType")
        holder.bindItems(items[position], context)

        Log.d("TAG", "items $items")
        if (ansType.equals("single select", true)){
            holder.binding.rbAnswers.visibility = View.VISIBLE
            holder.binding.chkAnswers.visibility = View.GONE
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            if (isServiceDeliverySheet){
                if (AppUtils.isServiceDeliveryFilled){
                    holder.binding.rbAnswers.isEnabled = false
                }
            }else{
                if (AppUtils.isInspectionDone){
                    holder.binding.rbAnswers.isEnabled = false
                }
            }
        }
        if (ansType.equals("multi select", true)){
            holder.binding.numberLayout.visibility = View.GONE
            holder.binding.spinnerLayout.visibility = View.GONE
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.VISIBLE
            if (isServiceDeliverySheet){
                if (AppUtils.isServiceDeliveryFilled){
                    holder.binding.chkAnswers.isEnabled = false
                }
            }else{
                if (AppUtils.isInspectionDone){
                    holder.binding.chkAnswers.isEnabled = false
                }
            }
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
        if (ansType.equals("DropdownSingleSelect", true)){
            //holder.binding.spnType.setText(qAnswer)
            holder.binding.spinnerLayout.visibility = View.VISIBLE
            /*if (holder.binding.numberEt.text.toString().trim() == ""){
                holder.binding.numberEt.setText("0")
            }*/
            holder.binding.rbAnswers.visibility = View.GONE
            holder.binding.chkAnswers.visibility = View.GONE
            if (isServiceDeliverySheet){
                if (AppUtils.isServiceDeliveryFilled){
                    holder.binding.spnType.isEnabled = false
                    holder.binding.spinnerLayout.background = ContextCompat.getDrawable(context, R.drawable.edit_box_border_disabled)
                }
            }else{
                if (AppUtils.isInspectionDone){
                    holder.binding.spnType.isEnabled = false
                    holder.binding.spinnerLayout.background = ContextCompat.getDrawable(context, R.drawable.edit_box_border_disabled)
                }
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
            onTextChangedListener?.onOptionChange(position, items[position]?.optionDisplayText.toString(), questionId, "radio")
            notifyDataSetChanged()
        }

        holder.binding.chkAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position]}")
            selectedPos = position
            items[position]?.isSelected = holder.binding.chkAnswers.isChecked
            onTextChangedListener?.onCheckboxClicked(position, holder.binding.chkAnswers.isChecked, items[position]?.optionDisplayText.toString(), questionId, "checkbox")
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
                if (selected != "Select Count"){
                    selectedPos = position
                    items[position]?.isSelected = true
                    for (i in 0 until items.size){
                        if (selectedPos != i) {
                            items[i]?.isSelected = false
                        }
                    }
                    onTextChangedListener?.onTextChange(position, selected, questionId)
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        /*holder.binding.numberEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                selectedPos = position
                onTextChangedListener?.onTextChange(position, p0.toString(), questionId)
            }
        })*/

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
        dropdownArr.clear()
        if (options != null) {
            items.addAll(options)
        }else{
            items.add(null)
        }
        Log.d("TAG", "$items")
        qAnswer = answer.toString()
        ansType = type.toString()
        questionId = id.toString().toInt()
        Log.d("TAG", "Ans Type $ansType")
    }
    fun addNumberText(answer: ArrayList<String>?, gAnswer: String?, type: String?, id: Int?){
        dropdownArr.clear()
        items.clear()
        items.add(null)
        dropdownArr.add(0, "Select Count")
        answer?.forEach {
            dropdownArr.add(it)
        }
        qAnswer = answer.toString()
        givenAnswer = gAnswer.toString()
        ansType = type.toString()
        questionId = id.toString().toInt()
        if (type.equals("DropdownSingleSelect", true)){
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
    }

    class MyHolder(val binding: LayoutTmsChildAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: QuestionOption?, context: Context){
            binding.chkAnswers.visibility = View.GONE
            binding.rbAnswers.text = item?.optionDisplayText
            binding.chkAnswers.text = item?.optionDisplayText
            binding.rbAnswers.isChecked = item?.isSelected==true
            binding.chkAnswers.isChecked = item?.isSelected==true
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