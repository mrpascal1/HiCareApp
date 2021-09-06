package com.ab.hicarerun.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.RowBarcodePestTypeBinding
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDDPestType

class PestTypeAdapter(val context: Context, val pestList: ArrayList<BarcodeDDPestType>) : RecyclerView.Adapter<PestTypeAdapter.MyHolder>() {

    lateinit var onEditTextChanged: OnEditTextChanged

    fun setOnEditTextChangedListener(onEditTextChanged: OnEditTextChanged){
        this.onEditTextChanged = onEditTextChanged
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodePestTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(pestList[position], position)
        holder.binding.countEt.setOnFocusChangeListener { view, b ->
            onEditTextChanged.onTextChanged(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }
        holder.binding.countEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                onEditTextChanged.onTextChanged(position, p0.toString(), pestList[position].barcodeId, pestList[position].id)
            }
        })
        if (holder.binding.countEt.text.toString().length != 0){
            onEditTextChanged.onTextChanged(position, holder.binding.countEt.text.toString(), pestList[position].barcodeId, pestList[position].id)
        }

        holder.binding.imgCapture.setOnClickListener {
            onEditTextChanged.onPictureIconClicked(position, pestList[position].barcodeId, pestList[position].id)
        }
    }

    override fun getItemCount(): Int {
        return pestList.size
    }

    class MyHolder(val binding: RowBarcodePestTypeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItems(pestList: BarcodeDDPestType, position: Int){
            binding.subTypeTv.text = pestList.sub_Type
            if (pestList.show_Count == true){
                binding.countEt.visibility = View.VISIBLE
            }else{
                binding.countEt.visibility = View.GONE
            }
            if (pestList.capture_Image == true){
                binding.lnrCapture.visibility = View.VISIBLE
            }else{
                binding.lnrCapture.visibility = View.GONE
            }
        }
    }
    interface OnEditTextChanged {
        fun onTextChanged(position: Int, charSeq: String, barcodeId: Int?, pestTypeId: Int?)
        fun onPictureIconClicked(position: Int, barcodeId: Int?, pestTypeId: Int?)
    }
}