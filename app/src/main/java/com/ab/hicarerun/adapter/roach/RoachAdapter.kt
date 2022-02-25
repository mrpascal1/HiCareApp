package com.ab.hicarerun.adapter.roach

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ItemRoachListBinding
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachList
import com.squareup.picasso.Picasso

class RoachAdapter(val context: Context): RecyclerView.Adapter<RoachAdapter.MyHolder>() {

    val items = ArrayList<RoachList>()
    var roachClickListener: RoachClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = ItemRoachListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, items[position])
        holder.binding.performBtn.setOnClickListener {
            roachClickListener?.uploadClick(position, items[position].id.toString().toInt(), items[position].deviceName.toString(), items[position].accountNo.toString(), items[position].deviceDisplayName.toString(), items[position].deviceLocationImageUrl.toString())
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
        fun bindItems(context: Context, data: RoachList){
            binding.nameTv.text = data.deviceDisplayName
            binding.locationTv.text = data.deployedLocation
            binding.createdOnTv.text = data.createdOnDisplay
            if (data.deviceLocationImageUrl != null && data.deviceLocationImageUrl != ""){
                binding.locationIv.visibility = View.VISIBLE
                Picasso.get().load(data.deviceLocationImageUrl).fit().into(binding.locationIv)
            }else{
                binding.locationIv.visibility = View.GONE
            }
            if (data.isDeviceUpdateDone == true){
                binding.performBtn.alpha = 0.6f
                binding.performBtn.isEnabled = false
                binding.performTv.text = "Performed"
                binding.performIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green))
                //binding.uploadedIv.visibility = View.VISIBLE
            }else{
                binding.performBtn.alpha = 1f
                binding.performBtn.isEnabled = true
                binding.performTv.text = "Perform"
                binding.performIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_list))
                //binding.uploadedIv.visibility = View.GONE
            }
        }
    }

    fun setOnRoachClickListener(onRoachClickListener: RoachClickListener){
        this.roachClickListener = onRoachClickListener
    }

    interface RoachClickListener {
        fun uploadClick(position: Int, deviceId: Int, deviceName: String, accountNo: String, deviceDisplay: String, url: String)
        fun deleteClick(position: Int, deviceId: Int)
    }
}