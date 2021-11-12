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

        if (items[position].isPictureRequired == true){
            holder.binding.relPhoto.visibility = View.VISIBLE
        }else{
            holder.binding.relPhoto.visibility = View.GONE
        }

        if (items[position].pictureURL != "" && items[position].pictureURL != null){
            Picasso.get().load(items[position].pictureURL).fit().into(holder.binding.imgUploadedCheque)
            holder.binding.lnrUpload.visibility = View.GONE
            holder.binding.lnrImage.visibility = View.VISIBLE
        }else{
            holder.binding.lnrUpload.visibility = View.VISIBLE
            holder.binding.lnrImage.visibility = View.GONE
        }

        /**
         * Image Upload 1,2,3
         * */
        holder.binding.lnrUpload.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 1)
        }

        holder.binding.lnrUpload2.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 2)
        }

        holder.binding.lnrUpload3.setOnClickListener {
            onCameraClickListener?.onCameraClicked(position, items[position].questionId, 3)
        }

        /**
         * Image cancel 1,2,3
        * */
        holder.binding.imageCancel.setOnClickListener {
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 1)
        }

        holder.binding.imageCancel.setOnClickListener {
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 2)
        }

        holder.binding.imageCancel.setOnClickListener {
            onCameraClickListener?.onCancelClicked(position, items[position].questionId, 3)
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