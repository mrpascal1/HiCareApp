package com.ab.hicarerun.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.QuizLeaderBoardAdapter
import com.ab.hicarerun.databinding.ActivityQuizLeaderBoardBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLBData
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLBResourceList
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLeaderBoardBase

class QuizLeaderBoard : BaseActivity() {

    lateinit var binding: ActivityQuizLeaderBoardBinding
    private var resourceId = ""
    lateinit var quizLeaderBoardAdapter: QuizLeaderBoardAdapter
    lateinit var quizLBData: ArrayList<QuizLBData>
    lateinit var quizLBResourceList: ArrayList<QuizLBResourceList>
    var highest = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizLeaderBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorBlue)
        }

        val loginRealmModels = BaseApplication.getRealm().where(LoginResponse::class.java).findAll()
        if (loginRealmModels != null && loginRealmModels.size > 0) {
            resourceId = loginRealmModels[0]!!.userID
        }

        quizLBData = ArrayList()
        quizLBResourceList = ArrayList()
        quizLeaderBoardAdapter = QuizLeaderBoardAdapter(this, quizLBResourceList, highest)
        val llManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        llManager.stackFromEnd = true
        binding.recyclerView.layoutManager = llManager
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.adapter = quizLeaderBoardAdapter

        binding.backIv.setOnClickListener {
            getBack()
        }

        getPuzzleLeaderBoard(resourceId)
    }

    private fun getPuzzleLeaderBoard(resourceId: String){
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<QuizLeaderBoardBase>{
            override fun onResponse(requestCode: Int, response: QuizLeaderBoardBase?) {
                val pointsArr = ArrayList<Int>()
                if (response != null){
                    for (i in 0 until response.data!!.size){
                        val data = response.data[i]
                        val levelName = data.levelName
                        for (j in 0 until data.resourceList!!.size){
                            val resourceList = data.resourceList[j]
                            val uLevelName = resourceList.levelName
                            val uResourceId = resourceList.resourceId
                            val resourceName = resourceList.resourceName
                            val isSelf = resourceList.isSelf
                            val resourceRank = resourceList.resourceRank
                            val points = resourceList.points
                            val lastPlayedOn = resourceList.lastPlayedOn
                            val lastPlayedOnDisplay = resourceList.lastPlayedOnDisplay
                            pointsArr.add(points.toString().toInt())
                            quizLBResourceList.add(QuizLBResourceList(uLevelName, uResourceId, resourceName, isSelf, resourceRank, points, lastPlayedOn, lastPlayedOnDisplay))
                        }
                        quizLBData.add(QuizLBData(levelName, quizLBResourceList))
                    }
                    highest = pointsArr.maxOrNull().toString().toInt()
                    QuizLeaderBoardBase(response.isSuccess, quizLBData, response.errorMessage, response.param1, response.responseMessage)
                }
                quizLeaderBoardAdapter.notifyDataSetChanged()
            }

            override fun onFailure(requestCode: Int) {
            }
        })
        controller.getPuzzleLeaderBoard(202124, resourceId)
    }

    private fun getBack(){
        val fragment = supportFragmentManager.backStackEntryCount
        if (fragment < 1){
            finish()
        }else{
            fragmentManager.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                getBack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}