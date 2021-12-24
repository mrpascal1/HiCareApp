package com.ab.hicarerun.adapter.tms

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.LayoutTmsParentAdapterBinding
import com.ab.hicarerun.network.models.TmsModel.Option
import com.ab.hicarerun.network.models.TmsModel.QuestionImageUrl
import com.ab.hicarerun.network.models.TmsModel.QuestionList
import com.ab.hicarerun.utils.AppUtils
import com.squareup.picasso.Picasso

class TmsQuestionsParentAdapter(val context: Context, private val isServiceDeliverySheet: Boolean) : RecyclerView.Adapter<TmsQuestionsParentAdapter.MyHolder>() {

    var items: ArrayList<QuestionList> = ArrayList()
    var optionList: ArrayList<Option> = ArrayList()
    val checkItems: HashMap<Int, String> = HashMap()
    var strAnswer = ""

    var onItemClickListener: OnItemClickListener? = null
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

        if (items[position].isPictureRequired == true) {
            holder.binding.relPhoto.visibility = View.VISIBLE
            Log.d("TAG", "Called this")

            if (items[position].pictureURL?.isNotEmpty() == true && items[position].pictureURL != null) {
                val foundArr = ArrayList<String>()
                var found1 = false
                var found2 = false
                var found3 = false
                items[position].pictureURL?.forEach {
                    foundArr.add(it)
                    /*if (it.id == 0) found1 = true
                if (it.id == 1) found2 = true
                if (it.id == 2) found3 = true*/
                }

                val arrSize = foundArr.size

                if (arrSize == 4) {
                    holder.binding.relPhoto.visibility = View.VISIBLE
                    holder.binding.relPhoto2.visibility = View.VISIBLE
                    holder.binding.relPhoto3.visibility = View.VISIBLE
                    holder.binding.relPhoto4.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![0]).fit()
                        .into(holder.binding.imgUploadedCheque)
                    holder.binding.lnrUpload.visibility = View.GONE
                    holder.binding.lnrImage.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![1]).fit()
                        .into(holder.binding.imgUploadedCheque2)
                    holder.binding.lnrUpload2.visibility = View.GONE
                    holder.binding.lnrImage2.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![2]).fit()
                        .into(holder.binding.imgUploadedCheque3)
                    holder.binding.lnrUpload3.visibility = View.GONE
                    holder.binding.lnrImage3.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![3]).fit()
                        .into(holder.binding.imgUploadedCheque4)
                    holder.binding.lnrUpload4.visibility = View.GONE
                    holder.binding.lnrImage4.visibility = View.VISIBLE

                }
                if (arrSize == 3) {
                    holder.binding.relPhoto.visibility = View.VISIBLE
                    holder.binding.relPhoto2.visibility = View.VISIBLE
                    holder.binding.relPhoto3.visibility = View.VISIBLE
                    holder.binding.relPhoto4.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![0]).fit()
                        .into(holder.binding.imgUploadedCheque)
                    holder.binding.lnrUpload.visibility = View.GONE
                    holder.binding.lnrImage.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![1]).fit()
                        .into(holder.binding.imgUploadedCheque2)
                    holder.binding.lnrUpload2.visibility = View.GONE
                    holder.binding.lnrImage2.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![2]).fit()
                        .into(holder.binding.imgUploadedCheque3)
                    holder.binding.lnrUpload3.visibility = View.GONE
                    holder.binding.lnrImage3.visibility = View.VISIBLE

                    holder.binding.lnrUpload4.visibility = View.VISIBLE
                    holder.binding.lnrImage4.visibility = View.GONE

                }
                if (arrSize == 2) {
                    holder.binding.relPhoto.visibility = View.VISIBLE
                    holder.binding.relPhoto2.visibility = View.VISIBLE
                    holder.binding.relPhoto3.visibility = View.VISIBLE
                    holder.binding.relPhoto4.visibility = View.GONE

                    Picasso.get().load(items[position].pictureURL!![0]).fit()
                        .into(holder.binding.imgUploadedCheque)
                    holder.binding.lnrUpload.visibility = View.GONE
                    holder.binding.lnrImage.visibility = View.VISIBLE

                    Picasso.get().load(items[position].pictureURL!![1]).fit()
                        .into(holder.binding.imgUploadedCheque2)
                    holder.binding.lnrUpload2.visibility = View.GONE
                    holder.binding.lnrImage2.visibility = View.VISIBLE

                    holder.binding.lnrUpload3.visibility = View.VISIBLE
                    holder.binding.lnrImage3.visibility = View.GONE

                    holder.binding.lnrUpload4.visibility = View.GONE
                    holder.binding.lnrImage4.visibility = View.GONE

                }
                if (arrSize == 1) {
                    holder.binding.relPhoto.visibility = View.VISIBLE
                    holder.binding.relPhoto2.visibility = View.VISIBLE
                    holder.binding.relPhoto3.visibility = View.GONE
                    holder.binding.relPhoto4.visibility = View.GONE

                    Picasso.get().load(items[position].pictureURL!![0]).fit()
                        .into(holder.binding.imgUploadedCheque)
                    holder.binding.lnrUpload.visibility = View.GONE
                    holder.binding.lnrImage.visibility = View.VISIBLE

                    holder.binding.lnrUpload2.visibility = View.VISIBLE
                    holder.binding.lnrImage2.visibility = View.GONE

                    holder.binding.lnrUpload3.visibility = View.GONE
                    holder.binding.lnrImage3.visibility = View.GONE

                    holder.binding.lnrUpload4.visibility = View.GONE
                    holder.binding.lnrImage4.visibility = View.GONE
                }
            } else {
                if (items[position].isPictureRequired == true) {
                    holder.binding.relPhoto.visibility = View.VISIBLE
                    holder.binding.relPhoto2.visibility = View.GONE
                    holder.binding.relPhoto3.visibility = View.GONE
                    holder.binding.relPhoto4.visibility = View.GONE

                    holder.binding.lnrUpload.visibility = View.VISIBLE
                    holder.binding.lnrImage.visibility = View.GONE
                }
            }
        }else{
            holder.binding.relPhoto.visibility = View.GONE
            holder.binding.relPhoto2.visibility = View.GONE
            holder.binding.relPhoto3.visibility = View.GONE
            holder.binding.relPhoto4.visibility = View.GONE
        }

        /**
         * Image Upload 1,2,3,4
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

        holder.binding.lnrUpload4.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 3)
        }

        /**
         * Image cancel 1,2,3,4
         * */
        holder.binding.imageCancel.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 1){
                items[position].pictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 0)
        }

        holder.binding.imageCancel2.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 2){
                items[position].pictureURL?.removeAt(1)
            }else{
                items[position].pictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 1)
        }

        holder.binding.imageCancel3.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 3){
                items[position].pictureURL?.removeAt(2)
            }else if(items[position].pictureURL?.size!! >= 2){
                items[position].pictureURL?.removeAt(1)
            }else {
                items[position].pictureURL?.removeAt(0)
            }

            /*when (items[position].pictureURL?.size) {
                3 -> {
                    items[position].pictureURL?.removeAt(2)
                }
                2 -> {
                    items[position].pictureURL?.removeAt(1)
                }
                else -> {
                    items[position].pictureURL?.removeAt(0)
                }
            }*/
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 2)
        }

        holder.binding.imageCancel4.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 4){
                items[position].pictureURL?.removeAt(3)
            }else if (items[position].pictureURL?.size!! >= 3){
                items[position].pictureURL?.removeAt(2)
            }else if(items[position].pictureURL?.size!! >= 2){
                items[position].pictureURL?.removeAt(1)
            }else {
                items[position].pictureURL?.removeAt(0)
            }
            /*when (items[position].pictureURL?.size) {
                3 -> {
                    items[position].pictureURL?.removeAt(2)
                }
                2 -> {
                    items[position].pictureURL?.removeAt(1)
                }
                else -> {
                    items[position].pictureURL?.removeAt(0)
                }
            }*/
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 3)
        }

        val answersChildAdapter = TmsAnswersChildAdapter(context, isServiceDeliverySheet)
        Log.d("TAG", "TYPE ${items[position].questionType}")
        if (items[position].questionType.equals("DropdownSingleSelect")){
            answersChildAdapter.addNumberText(items[position].questionStrOption, items[position].answer, items[position].questionType, items[position].questionId)
        }else {
            answersChildAdapter.addData(
                items[position].questionOption,
                answer = items[position].answer,
                items[position].questionType,
                items[position].questionId
            )
        }
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
                onItemClickListener?.onItemClicked(position, questionId, str)
            }

            override fun onOptionChange(childPosition: Int, str: String, questionId: Int, type: String) {
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = str
                    }
                }
                onItemClickListener?.onItemClicked(position, questionId, str)
            }

            override fun onCheckboxClicked(childPosition: Int, isChecked: Boolean, str: String, questionId: Int, type: String) {
                val optionValue = items[position].questionOption?.get(childPosition)?.optionText

                if (isChecked){
                    val newAppendValue = if (checkItems[position] != null) checkItems[position].toString() + "," + optionValue else optionValue
                    checkItems[position] = newAppendValue.toString()
                }else{
                    var newAppendValue = checkItems[position]
                    if (newAppendValue != null) {
                        newAppendValue = newAppendValue.replace(",$optionValue", "")
                        newAppendValue = newAppendValue.replace(optionValue.toString(), "")
                        checkItems[position] = newAppendValue
                    }
                }
                strAnswer = if (checkItems[position] == null) "" else checkItems[position].toString()
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = strAnswer
                    }
                }
                onItemClickListener?.onItemClicked(position, questionId, strAnswer)
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
        if (isServiceDeliverySheet){
            if (AppUtils.isServiceDeliveryFilled) {
                holder.binding.relPhoto.isEnabled = false
                holder.binding.relPhoto2.isEnabled = false
                holder.binding.relPhoto3.isEnabled = false
                holder.binding.relPhoto4.isEnabled = false


                holder.binding.lnrUpload.isEnabled = false
                holder.binding.lnrUpload2.isEnabled = false
                holder.binding.lnrUpload3.isEnabled = false
                holder.binding.lnrUpload4.isEnabled = false

                holder.binding.imageCancel.isEnabled = false
                holder.binding.imageCancel2.isEnabled = false
                holder.binding.imageCancel3.isEnabled = false
                holder.binding.imageCancel4.isEnabled = false
            }
        }else{
            if (AppUtils.isInspectionDone){
                holder.binding.relPhoto.isEnabled = false
                holder.binding.relPhoto2.isEnabled = false
                holder.binding.relPhoto3.isEnabled = false
                holder.binding.relPhoto4.isEnabled = false


                holder.binding.lnrUpload.isEnabled = false
                holder.binding.lnrUpload2.isEnabled = false
                holder.binding.lnrUpload3.isEnabled = false
                holder.binding.lnrUpload4.isEnabled = false

                holder.binding.imageCancel.isEnabled = false
                holder.binding.imageCancel2.isEnabled = false
                holder.binding.imageCancel3.isEnabled = false
                holder.binding.imageCancel4.isEnabled = false
            }
        }
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
            binding.txtQuest.text = item.questionDisplayText
        }
    }

    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    fun setOnCameraClickHandler(onCameraClickListener: OnCameraClickListener){
        this.onCameraClickListener = onCameraClickListener
    }

    interface OnCameraClickListener{
        fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int)
        fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int)
    }

    interface OnItemClickListener{
        fun onItemClicked(position: Int, questionId: Int?, answer: String)
    }
}