package com.ab.hicarerun.adapter.roach

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.ItemRoachListBinding
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachList

class RoachAdapter(val context: Context): RecyclerView.Adapter<RoachAdapter.MyHolder>() {

    val items = ArrayList<RoachList>()
    var roachClickListener: RoachClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemRoachListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        holder.binding.uploadBtn.setOnClickListener {
            roachClickListener?.uploadClick(position, items[position].deviceName.toString(), items[position].accountNo.toString())
        }
        holder.binding.deleteBtn.setOnClickListener {
            roachClickListener?.deleteClick(position, items[position].id.toString().toInt())
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toString().toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: List<RoachList>?){
        this.items.clear()
        if (items != null){
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    class MyHolder(val binding: ItemRoachListBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(data: RoachList){
            binding.nameTv.text = data.deviceDisplayName
            binding.locationTv.text = data.deployedLocation
            binding.createdOnTv.text = data.createdOnDisplay
            if (data.isDeviceUpdateDone == true){
                binding.uploadBtn.visibility = View.GONE
                binding.uploadedIv.visibility = View.VISIBLE
            }else{
                binding.uploadBtn.visibility = View.VISIBLE
                binding.uploadedIv.visibility = View.GONE
            }
        }
    }

    fun setOnRoachClickListener(onRoachClickListener: RoachClickListener){
        this.roachClickListener = onRoachClickListener
    }

    interface RoachClickListener {
        fun uploadClick(position: Int, deviceName: String, accountNo: String)
        fun deleteClick(position: Int, deviceId: Int)
    }
}