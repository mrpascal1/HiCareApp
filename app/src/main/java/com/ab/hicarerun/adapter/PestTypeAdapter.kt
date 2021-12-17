package com.ab.hicarerun.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowBarcodePestTypeBinding
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDDPestType
import com.ab.hicarerun.network.models.TSScannerModel.Option_List
import com.squareup.picasso.Picasso

class PestTypeAdapter(val context: Context, val pestList: ArrayList<BarcodeDDPestType>, val arrayAdapter: ArrayAdapter<String>) : RecyclerView.Adapter<PestTypeAdapter.MyHolder>() {

    lateinit var onEditTextChanged: OnEditTextChanged
    var isChanged = false
    var changeFrom = -1
    var reset = false

    fun setOnEditTextChangedListener(onEditTextChanged: OnEditTextChanged){
        this.onEditTextChanged = onEditTextChanged
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodePestTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(context, view)
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(pestList[position], position, arrayAdapter)
        //holder.binding.countEt.requestFocus()
        /*if (holder.binding.spnType.selectedItem.toString() != "" || holder.binding.spnType.selectedItem.toString() != "Select Option"){
            onEditTextChanged.onTextChanged(position, holder.binding.spnType.selectedItem.toString(), pestList[position].barcodeId, pestList[position].id)
        }*/
        holder.binding.spnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (holder.binding.spnType.selectedItem.toString().equals("Select Option", true)){
                    onEditTextChanged.onTextChanged(position, "", pestList[position].barcodeId, pestList[position].id)
                }else{
                    onEditTextChanged.onTextChanged(position, holder.binding.spnType.selectedItem.toString(), pestList[position].barcodeId, pestList[position].id)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        holder.binding.countEt.setOnFocusChangeListener { view, b ->
            onEditTextChanged.onTextChanged(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }
        holder.binding.countEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.toString() != "") {
                    if (!isChanged) {
                        if (p0.toString().toInt() >= 1) {
                            pestList.forEach {
                                if (it.sub_Type.equals("Change glue pad", true)) {
                                    if (it.show_Option == true) {
                                        it.pest_Count = "Yes"
                                        isChanged = true
                                        reset = true
                                        changeFrom = position
                                        notifyDataSetChanged()
                                        return@forEach
                                    }
                                }else if (it.sub_Type.equals("Replace cake", true)) {
                                    if (it.show_Option == true) {
                                        it.pest_Count = "Yes"
                                        isChanged = true
                                        reset = true
                                        changeFrom = position
                                        notifyDataSetChanged()
                                        return@forEach
                                    }
                                }
                            }
                        }
                    }
                }
                onEditTextChanged.onTextChanged(position, p0.toString(), pestList[position].barcodeId, pestList[position].id)
            }
        })
        if (holder.binding.countEt.text.toString().isNotEmpty()){
            onEditTextChanged.onTextChanged(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }

        holder.binding.imgCapture.setOnClickListener {
            onEditTextChanged.onPictureIconClicked(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }

        holder.binding.cancelLayout.setOnClickListener {
            onEditTextChanged.onCancelIconClicked(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }
        if (pestList[position].show_Option == true){
            if (pestList[position].sub_Type.equals("Change glue pad", true)) {
                if (pestList[position].pest_Count != null && pestList[position].pest_Count != "") {
                    if (pestList[position].pest_Count.equals("Yes", true)) {
                        holder.binding.spnType.setSelection(1)
                        isChanged = true
                    }
                }
            }else if (pestList[position].sub_Type.equals("Replace cake", true)) {
                if (pestList[position].pest_Count != null && pestList[position].pest_Count != "") {
                    if (pestList[position].pest_Count.equals("Yes", true)) {
                        holder.binding.spnType.setSelection(1)
                        isChanged = true
                    }
                }
            }
        }
        if (reset){
            if (position == changeFrom){
                holder.binding.countEt.requestFocus()
                reset = false
            }
        }
        if (pestList[position].show_Count == true) {
            val length = holder.binding.countEt.length()
            val text = holder.binding.countEt.text
            Selection.setSelection(text, length)
        }
    }

    override fun getItemCount(): Int {
        return pestList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyHolder(val context: Context, val binding: RowBarcodePestTypeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItems(pestList: BarcodeDDPestType, position: Int, arrayAdapter: ArrayAdapter<String>){
            binding.subTypeTv.text = pestList.sub_Type
            binding.countEt.setText(pestList.pest_Count)
            //binding.spnType.setSelection(binding.spnType.selectedItemPosition)
            if (binding.spnType.adapter == null){
                val optionType = ArrayList<String>()
                optionType.add("Select Option")
                pestList.option_List?.forEach {
                    optionType.add(it.text.toString())
                }
                val arrayAdapter2 = object : ArrayAdapter<String>(context, R.layout.spinner_layout_new, optionType){
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
                arrayAdapter2.setDropDownViewResource(R.layout.spinner_popup)
                binding.spnType.adapter = arrayAdapter2
            }

            if (pestList.show_Count == true){
                binding.countEt.visibility = View.VISIBLE
            }else{
                binding.countEt.visibility = View.GONE
            }
            if (pestList.capture_Image == true){
                binding.imageLayout.visibility = View.VISIBLE
            }else{
                binding.imageLayout.visibility = View.GONE
            }
            if (pestList.show_Option == true){
                binding.optionsSpinner.visibility = View.VISIBLE
            }else{
                binding.optionsSpinner.visibility = View.GONE
            }
            if (pestList.image_Url != null){
                Picasso.get().load(pestList.image_Url).placeholder(R.drawable.progress).fit().into(binding.imgCaptured)
                binding.imgCapture.visibility = View.GONE
                binding.imgCaptured.visibility = View.VISIBLE
                binding.cancelLayout.visibility = View.VISIBLE
            }else{
                binding.imgCapture.visibility = View.VISIBLE
                binding.imgCaptured.visibility = View.GONE
                binding.cancelLayout.visibility = View.GONE
            }
        }
    }
    interface OnEditTextChanged {
        fun onTextChanged(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?)
        fun onOptionTypeChanged(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?)
        fun onPictureIconClicked(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?)
        fun onCancelIconClicked(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?)
    }
}