package com.ab.hicarerun.adapter.pulse

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter
import com.ab.hicarerun.databinding.LayoutPulseParentBinding
import com.ab.hicarerun.network.models.pulsemodel.QuestionList
import com.ab.hicarerun.network.models.pulsemodel.SubQuestionList
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.AppUtils.checkItems
import com.ab.hicarerun.utils.ImageOverlayStfalcon
import com.squareup.picasso.Picasso

class PulseQuestionAdapter(val context: Context): RecyclerView.Adapter<PulseQuestionAdapter.MyHolder>(){

    val items = ArrayList<QuestionList>()
    val showSubList = ArrayList<String>()
    val hashMap = HashMap<String, List<SubQuestionList>>()
    //val checkItems: HashMap<Int, String> = HashMap()
    var strAnswer = ""
    var onItemClickListener: OnItemClickListener? = null
    var onCameraClickListener: OnCameraClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutPulseParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])

        if (AppUtils.isPulseSubmitted){
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
        holder.binding.lnrUpload.upload(position, items[position].questionId, 0, false)
        holder.binding.lnrUpload2.upload(position, items[position].questionId, 1, false)
        holder.binding.lnrUpload3.upload(position, items[position].questionId, 2, false)
        holder.binding.lnrUpload4.upload(position, items[position].questionId, 3, false)

        holder.binding.imageCancel.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 1){
                items[position].pictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 0, false)
        }

        holder.binding.imageCancel2.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 2){
                items[position].pictureURL?.removeAt(1)
            }else{
                items[position].pictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 1, false)
        }

        holder.binding.imageCancel3.setOnClickListener {
            if (items[position].pictureURL?.size!! >= 3){
                items[position].pictureURL?.removeAt(2)
            }else if(items[position].pictureURL?.size!! >= 2){
                items[position].pictureURL?.removeAt(1)
            }else {
                items[position].pictureURL?.removeAt(0)
            }
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 2, false)
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
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 3,false)
        }

        val answersAdapter = PulseAnswerChildAdapter(context)
        holder.binding.recycleChild.config(answersAdapter)
        val sublistQuestionAdapter = PulseSublistQuestionAdapter(context)
        holder.binding.recycleSubChild.config(sublistQuestionAdapter)
        answersAdapter.addData(
            items[position].questionOption,
            items[position].questionText,
            items[position].questionId,
            items[position].questionType,
            items[position].answer,
            items[position].isDisabled,
            items[position].questionStrOption)
        items[position].questionOption?.forEach {
            if (it.isSubQuestion == true && !it.subQuestionList.isNullOrEmpty()){
                sublistQuestionAdapter.addData(it.subQuestionList)
            }
        }
        answersAdapter.setOnListPresentListener(object : PulseAnswerChildAdapter.OnSubListPresentListener{
            override fun sendAndNotify(p: Int, qId: String, isSublistPresent: Boolean?, subList: List<SubQuestionList>?) {
                if (isSublistPresent == true && !subList.isNullOrEmpty()){
                    if (!showSubList.contains(qId)){
                        showSubList.add(qId)
                        hashMap[qId] = subList
                    }
                    holder.binding.recycleSubChild.visibility = View.VISIBLE
                }else{
                    if (showSubList.contains(qId)){
                        showSubList.remove(qId)
                        clearSelected(qId, hashMap[qId])
                        hashMap.remove(qId)
                        sublistQuestionAdapter.notifyDataSetChanged()
                    }
                    holder.binding.recycleSubChild.visibility = View.GONE
                }
                Log.d("TAG", "$showSubList")
            }
        })
        answersAdapter.setOnChildTextChangedListener(object : PulseAnswerChildAdapter.OnTextChangedListener{
            override fun onTextChange(childPosition: Int, str: String, questionId: Int) {
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = str
                    }
                }
                //onItemClickListener?.onItemClicked(position, questionId, str, subListClick)
            }

            override fun onOptionChange(childPosition: Int, str: String, questionId: Int, type: String) {
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = str
                    }
                }
                //onItemClickListener?.onItemClicked(position, questionId, str, subListClick)

            }

            override fun onCheckboxClicked(childPosition: Int, isChecked: Boolean, str: String, questionId: Int, type: String) {
                Log.d("TAG", "Parent Here")
                val optionValue = items[position].questionOption?.get(childPosition)?.optionText

                if (isChecked){
                    val newAppendValue = if (checkItems[questionId] != null) checkItems[questionId].toString() + "," + optionValue else optionValue
                    checkItems[questionId] = newAppendValue.toString()
                }else{
                    var newAppendValue = checkItems[questionId]
                    if (newAppendValue != null) {
                        newAppendValue = newAppendValue.replace(",$optionValue", "")
                        newAppendValue = newAppendValue.replace(optionValue.toString(), "")
                        checkItems[questionId] = newAppendValue
                    }
                }
                strAnswer = if (checkItems[questionId] == null) "" else checkItems[questionId].toString()
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = strAnswer
                    }
                }
                //onItemClickListener?.onItemClicked(position, questionId, strAnswer, subListClick)
            }

            override fun onChipClicked(childPosition: Int, isChecked: Boolean, str: String, questionId: Int) {
                val optionValue = str

                if (isChecked){
                    val newAppendValue = if (checkItems[questionId] != null) checkItems[questionId].toString() + "," + optionValue else optionValue
                    checkItems[questionId] = newAppendValue.toString()
                }else{
                    var newAppendValue = checkItems[questionId]
                    if (newAppendValue != null) {
                        newAppendValue = newAppendValue!!.replace(",$optionValue", "")
                        newAppendValue = newAppendValue!!.replace(optionValue.toString(), "")
                        checkItems[questionId] = newAppendValue!!
                    }
                }
                strAnswer = if (checkItems[questionId] == null) "" else checkItems[questionId].toString()
                items.forEach {
                    if (it.questionId == questionId){
                        it.answer = strAnswer
                    }
                }
                Log.d("TAG", strAnswer)
            }
        })
        sublistQuestionAdapter.setOnCameraClickHandler(object : PulseSublistQuestionAdapter.OnCameraClickListener {
            override fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean) {
                onCameraClickListener?.onCameraClicked(position, questionId, clickedBy, sublistClick)
            }

            override fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean) {
                onCameraClickListener?.onCancelClicked(position, questionId, clickedBy, sublistClick)
            }
        })

        if (showSubList.contains(items[position].questionId.toString())){
            Log.d("TAG", "Matched")
            Log.d("Sublist", "$showSubList")
            //sublistQuestionAdapter.addData(hashMap[items[position].questionId.toString()])
            holder.binding.recycleSubChild.visibility = View.VISIBLE
        }else{
            holder.binding.recycleSubChild.visibility = View.GONE
        }

        holder.binding.imgUploadedCheque.setOnClickListener {
            val arrayList = arrayOf(items[position].pictureURL!![0])
            ImageOverlayStfalcon(context, arrayList)
        }

        holder.binding.imgUploadedCheque2.setOnClickListener {
            val arrayList = arrayOf(items[position].pictureURL!![1])
            ImageOverlayStfalcon(context, arrayList)
        }

        holder.binding.imgUploadedCheque3.setOnClickListener {
            val arrayList = arrayOf(items[position].pictureURL!![2])
            ImageOverlayStfalcon(context, arrayList)
        }

        holder.binding.imgUploadedCheque4.setOnClickListener {
            val arrayList = arrayOf(items[position].pictureURL!![3])
            ImageOverlayStfalcon(context, arrayList)
        }
    }

    fun clearSelected(qId: String, subQuestionList: List<SubQuestionList>?){
        subQuestionList?.forEach {
            Log.d("TAG", "clearing id ${it.questionId}")
            it.answer = null
            it.pictureURL = arrayListOf()
            if (checkItems.containsKey(it.questionId)){
                Log.d("TAG", "clearing from map ${it.questionId}")
                checkItems.remove(it.questionId)
            }
            it.questionOption?.forEach { q ->
                q.isSelected = false
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun RecyclerView.config(answerChildAdapter: PulseAnswerChildAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = answerChildAdapter
    }
    private fun RecyclerView.config(sublistQuestionAdapter: PulseSublistQuestionAdapter){
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        clipToPadding = false
        adapter = sublistQuestionAdapter
    }

    private fun LinearLayout.upload(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean){
        setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, questionId, clickedBy, sublistClick)
        }
    }


    fun addData(items: ArrayList<QuestionList>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutPulseParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(question: QuestionList){
            binding.txtQuest.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            binding.txtQuest.text = "-> ${question.questionDisplayText}"
        }
    }

    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    fun setOnCameraClickHandler(onCameraClickListener: OnCameraClickListener){
        this.onCameraClickListener = onCameraClickListener
    }

    interface OnCameraClickListener{
        fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean)
        fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean)
    }

    interface OnItemClickListener{
        fun onItemClicked(position: Int, questionId: Int?, answer: String, sublistClick: Boolean)
    }
}