package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutTmsChildAdapterBinding
import com.ab.hicarerun.network.models.TmsModel.Option

class TmsAnswersChildAdapter(val context: Context) : RecyclerView.Adapter<TmsAnswersChildAdapter.MyHolder>() {

    var items: ArrayList<Option> = ArrayList()
    var selectedPos = -1
    var isSelected = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutTmsChildAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])


        holder.binding.rbAnswers.setOnClickListener {
            Log.d("TAG: ", "Position $position and ID ${items[position].id}")
            selectedPos = position
            items[position].selected = true
            holder.binding.rbAnswers.isChecked = true
            for (i in 0 until items.size){
                if (selectedPos != i) {
                    holder.binding.rbAnswers.isChecked = false;
                    items[i].selected = false
                }
            }
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(options: List<Option>?, answerSelected: Boolean){
        items.clear()
        if (options != null) {
            items.addAll(options)
        }
        isSelected = answerSelected
    }

    class MyHolder(val binding: LayoutTmsChildAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: Option){
            binding.chkAnswers.visibility = View.GONE
            binding.rbAnswers.text = item.option
            binding.rbAnswers.isChecked = item.selected
        }
    }
}