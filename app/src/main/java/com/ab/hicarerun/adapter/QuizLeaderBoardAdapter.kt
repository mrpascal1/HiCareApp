package com.ab.hicarerun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowQuizLeaderboardBinding
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLBResourceList
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLeaderBoardBase

class QuizLeaderBoardAdapter(val context: Context, val quizLBResourceList: List<QuizLBResourceList>, var highest: Int) : RecyclerView.Adapter<QuizLeaderBoardAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowQuizLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(context, view, highest)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(quizLBResourceList[position])
    }

    override fun getItemCount(): Int {
        return quizLBResourceList.size
    }

    class MyHolder(val context: Context, val binding: RowQuizLeaderboardBinding, val highest: Int): RecyclerView.ViewHolder(binding.root){
        fun bindItems(quizLBResourceList: QuizLBResourceList){
            val levelName = quizLBResourceList.levelName
            binding.userNameTv.text = quizLBResourceList.resourceName
            binding.pointsTv.text = "${quizLBResourceList.points} pts"
            binding.indexTv.text = (adapterPosition+1).toString()
            binding.levelNameTv.text = "Level : ${quizLBResourceList.levelName}"
            if (levelName.equals("Basic", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_bronze))
            }
            if (levelName.equals("Intermediate", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_silver))
            }
            if (levelName.equals("Expert", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_gold))
            }
        }
    }
}