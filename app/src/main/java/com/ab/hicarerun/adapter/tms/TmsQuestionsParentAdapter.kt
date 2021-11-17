package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutQuestionParentAdapterBinding
import com.ab.hicarerun.databinding.LayoutTmsParentAdapterBinding
import com.ab.hicarerun.handler.OnListItemClickHandler
import com.ab.hicarerun.network.models.TmsModel.*
import com.squareup.picasso.Picasso

class TmsQuestionsParentAdapter(val context: Context) : RecyclerView.Adapter<TmsQuestionsParentAdapter.MyHolder>() {

    var items: ArrayList<QuestionList> = ArrayList()
    var optionList: ArrayList<Option> = ArrayList()
    var onItemClickHandler: OnListItemClickHandler? = null
    var onCameraClickListener: OnCameraClickListener? = null
    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutTmsParentAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(context, view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])

        /*if (items[position].isPictureRequired == true){
            holder.binding.relPhoto.visibility = View.VISIBLE
        }else{
            holder.binding.relPhoto.visibility = View.GONE
        }*/

        if (items[position].isPictureRequired == true){
            holder.binding.relPhoto.visibility = View.VISIBLE
            Log.d("TAG", "Called this")
        }else{
            holder.binding.relPhoto.visibility = View.GONE
            holder.binding.relPhoto2.visibility = View.GONE
            holder.binding.relPhoto3.visibility = View.GONE
        }
        if (items[position].qPictureURL?.isNotEmpty() == true && items[position].qPictureURL != null){
            val foundArr = ArrayList<Int>()
            var found1 = false
            var found2 = false
            var found3 = false
            items[position].qPictureURL?.forEach {
                foundArr.add(it.id)
                if (it.id == 0) found1 = true
                if (it.id == 1) found2 = true
                if (it.id == 2) found3 = true
            }

            val arrSize = foundArr.size
            if (arrSize == 3){
                holder.binding.relPhoto.visibility = View.VISIBLE
                holder.binding.relPhoto2.visibility = View.VISIBLE
                holder.binding.relPhoto3.visibility = View.VISIBLE

                Picasso.get().load(items[position].qPictureURL!![0].url).fit()
                    .into(holder.binding.imgUploadedCheque)
                holder.binding.lnrUpload.visibility = View.GONE
                holder.binding.lnrImage.visibility = View.VISIBLE

                Picasso.get().load(items[position].qPictureURL!![1].url).fit()
                    .into(holder.binding.imgUploadedCheque2)
                holder.binding.lnrUpload2.visibility = View.GONE
                holder.binding.lnrImage2.visibility = View.VISIBLE

                Picasso.get().load(items[position].qPictureURL!![2].url).fit()
                    .into(holder.binding.imgUploadedCheque3)
                holder.binding.lnrUpload3.visibility = View.GONE
                holder.binding.lnrImage3.visibility = View.VISIBLE

            }
            if (arrSize == 2){
                holder.binding.relPhoto.visibility = View.VISIBLE
                holder.binding.relPhoto2.visibility = View.VISIBLE
                holder.binding.relPhoto3.visibility = View.VISIBLE

                Picasso.get().load(items[position].qPictureURL!![0].url).fit()
                    .into(holder.binding.imgUploadedCheque)
                holder.binding.lnrUpload.visibility = View.GONE
                holder.binding.lnrImage.visibility = View.VISIBLE

                Picasso.get().load(items[position].qPictureURL!![1].url).fit()
                    .into(holder.binding.imgUploadedCheque2)
                holder.binding.lnrUpload2.visibility = View.GONE
                holder.binding.lnrImage2.visibility = View.VISIBLE

                holder.binding.lnrUpload3.visibility = View.VISIBLE
                holder.binding.lnrImage3.visibility = View.GONE

            }
            if (arrSize == 1){
                holder.binding.relPhoto.visibility = View.VISIBLE
                holder.binding.relPhoto2.visibility = View.VISIBLE
                holder.binding.relPhoto3.visibility = View.GONE

                Picasso.get().load(items[position].qPictureURL!![0].url).fit()
                    .into(holder.binding.imgUploadedCheque)
                holder.binding.lnrUpload.visibility = View.GONE
                holder.binding.lnrImage.visibility = View.VISIBLE

                holder.binding.lnrUpload2.visibility = View.VISIBLE
                holder.binding.lnrImage2.visibility = View.GONE

                holder.binding.lnrUpload3.visibility = View.GONE
                holder.binding.lnrImage3.visibility = View.GONE
            }
        }else{
            if (items[position].isPictureRequired == true){
                holder.binding.relPhoto.visibility = View.VISIBLE
                holder.binding.relPhoto2.visibility = View.GONE
                holder.binding.relPhoto3.visibility = View.GONE

                holder.binding.lnrUpload.visibility = View.VISIBLE
                holder.binding.lnrImage.visibility = View.GONE
            }

        }

        /**
         * Image Upload 1,2,3
         * */
        holder.binding.lnrUpload.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 0)
        }

        holder.binding.lnrUpload2.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 1)
        }

        holder.binding.lnrUpload3.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 2)
        }

        /**
         * Image cancel 1,2,3
         * */
        holder.binding.imageCancel.setOnClickListener {
            if (items[position].qPictureURL?.size!! >= 1){
                items[position].qPictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 0)
        }

        holder.binding.imageCancel2.setOnClickListener {
            if (items[position].qPictureURL?.size!! >= 2){
                items[position].qPictureURL?.removeAt(1)
            }else{
                items[position].qPictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 1)
        }

        holder.binding.imageCancel3.setOnClickListener {
            when {
                items[position].qPictureURL?.size!! == 3 -> {
                    items[position].qPictureURL?.removeAt(2)
                }
                items[position].qPictureURL?.size!! == 2 -> {
                    items[position].qPictureURL?.removeAt(1)
                }
                else -> {
                    items[position].qPictureURL?.removeAt(0)
                }
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 2)
        }

        val answersChildAdapter = TmsAnswersChildAdapter(context)
        Log.d("TAG", "TYPE ${items[position].questionType}")
        answersChildAdapter.addData(items[position].questionOption, answer = items[position].answer, items[position].questionType, items[position].questionId)

/*        if (items[position].questionType.equals("numberText", true)){
            answersChildAdapter.addData(items[position].questionOption, answer = items[position].answer, items[position].questionType, items[position].questionId)
            //answersChildAdapter.addNumberText(items[position].answer, items[position].questionType, items[position].questionId)
        }else{
        }*/
        answersChildAdapter.setOnChildTextChangedListener(object : TmsAnswersChildAdapter.OnTextChangedListener{
            override fun onTextChange(childPosition: Int, str: String, questionId: Int) {
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = str
                    }
                }
            }

            override fun onOptionChange(childPosition: Int, str: String, questionId: Int) {

            }
        })

        holder.binding.recycleChild.layoutManager = LinearLayoutManager(context)
        holder.binding.recycleChild.setHasFixedSize(true)
        holder.binding.recycleChild.clipToPadding = false
        holder.binding.recycleChild.adapter = answersChildAdapter


        /*holder.itemView.setOnClickListener {
            selectedPos = position
            onItemClickHandler?.onItemClick(position)
            notifyDataSetChanged()
        }*/
    }

    fun maintainVisibility(holder: MyHolder, item: List<QuestionImageUrl>){
        var found0 = false
        var found1 = false
        var found2 = false
        item.forEach {
            if (it.id == 0)found0 = true
            if (it.id == 1)found1 = true
            if (it.id == 2)found2 = true
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(item: List<QuestionList>){
        items.clear()
        if (item != null) {
            items.addAll(item)
        }
    }


    class MyHolder(val context: Context, val binding: LayoutTmsParentAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: QuestionList) {
            binding.txtQuest.text = item.questionText
        }
    }

    fun setOnClickHandler(onItemClickHandler: OnListItemClickHandler){
        this.onItemClickHandler = onItemClickHandler
    }

    fun setOnCameraClickHandler(onCameraClickListener: OnCameraClickListener){
        this.onCameraClickListener = onCameraClickListener
    }

    interface OnCameraClickListener{
        fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int)
        fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int)
    }
}