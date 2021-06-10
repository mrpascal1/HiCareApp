package com.ab.hicarerun.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowBarcodeItemBinding
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeList

class BarcodeAdapter(val context: Context, val barcodeList: List<BarcodeList>) : RecyclerView.Adapter<BarcodeAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, barcodeList[position])
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

    class MyHolder(val binding: RowBarcodeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(context: Context, barcodeList: BarcodeList){
            val barcode_data = barcodeList.barcode_Data.toString()
            val isVerified = barcodeList.isVerified
            if (isVerified == true){
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green))
            }else{
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black))
            }
            binding.barcodeTv.text = barcode_data
            Log.d("TAG-Adapter", barcode_data)
        }
    }
}