package com.ab.hicarerun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutReferralInterestAdapterBinding
import com.ab.hicarerun.network.models.TSScannerModel.Pest_Type

class PestTypeAdapter(val context: Context, val pestList: ArrayList<Pest_Type>) : RecyclerView.Adapter<PestTypeAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutReferralInterestAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(pestList[position])
    }

    override fun getItemCount(): Int {
        return pestList.size
    }

    class MyHolder(val binding: LayoutReferralInterestAdapterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItems(pestList: Pest_Type){
            binding.txtName.text = pestList.sub_Type
        }
    }
}