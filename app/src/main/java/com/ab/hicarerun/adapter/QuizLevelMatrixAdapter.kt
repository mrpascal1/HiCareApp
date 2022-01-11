package com.ab.hicarerun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.databinding.IncentiveMatrixAdapterBinding
import com.ab.hicarerun.network.models.quizlevelmodel.QuizLevelData

class QuizLevelMatrixAdapter(val context: Context, val quizLevelData: List<QuizLevelData>): RecyclerView.Adapter<QuizLevelMatrixAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = IncentiveMatrixAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(quizLevelData[position])
    }

    override fun getItemCount(): Int {
        return quizLevelData.size
    }

    class MyHolder(val incentiveMatrixAdapterBinding: IncentiveMatrixAdapterBinding):
        RecyclerView.ViewHolder(incentiveMatrixAdapterBinding.root){
            fun bindItems(quizLevelData: QuizLevelData){
                incentiveMatrixAdapterBinding.txtMatrix.text = quizLevelData.levelName
                incentiveMatrixAdapterBinding.txtIncentive.text = quizLevelData.pointsInfo
            }
    }
}