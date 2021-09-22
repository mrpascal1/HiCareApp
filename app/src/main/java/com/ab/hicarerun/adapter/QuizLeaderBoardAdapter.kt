package com.ab.hicarerun.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.LeaderBoardAdapterBinding
import com.ab.hicarerun.databinding.RowQuizLeaderboardBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLBResourceList

class QuizLeaderBoardAdapter(val context: Context, val quizLBResourceList: List<QuizLBResourceList>, var highest: Int) : RecyclerView.Adapter<QuizLeaderBoardAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowQuizLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(context, view, highest)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(quizLBResourceList[position])
        if (quizLBResourceList[position].resourceId != null) {
                val controller = NetworkCallController()
                controller.setListner(object : NetworkResponseListner<Any?> {
                    override fun onResponse(requestCode: Int, response: Any?) {
                        val base64 = response as String
                        val decodedString = Base64.decode(base64, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        if (base64.length > 0) {
                            holder.binding.profileIv.setImageBitmap(decodedByte)
                        }
                    }
                    override fun onFailure(requestCode: Int) {}
                })
                controller.getResourceProfilePicture(202129, quizLBResourceList[position].resourceId)
        }
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
            binding.levelNameTv.text = "${quizLBResourceList.levelNameDisplay}"
            /*if (levelName.equals("Basic", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_bronze))
            }
            if (levelName.equals("Intermediate", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_silver))
            }
            if (levelName.equals("Expert", ignoreCase = true)){
                binding.awardIv.setImageDrawable(context.resources.getDrawable(R.drawable.ic_award_gold))
            }*/
        }
    }
}