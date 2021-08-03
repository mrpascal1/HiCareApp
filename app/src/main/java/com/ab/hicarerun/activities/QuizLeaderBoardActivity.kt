package com.ab.hicarerun.activities

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
import com.ab.hicarerun.utils.AppUtils
import de.hdodenhof.circleimageview.CircleImageView

class QuizLeaderBoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuizLeaderBoardBinding
    private var resourceId = ""
    lateinit var quizLeaderBoardAdapter: QuizLeaderBoardAdapter
    lateinit var quizLBData: ArrayList<QuizLBData>
    lateinit var quizLBResourceList: ArrayList<QuizLBResourceList>
    var highest = 0
    var myPoints = 0

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
        //getResourcePic(resourceId)
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
                            if (resourceRank == 1){
                                binding.txtFirstName.text = resourceName
                                binding.txtFirstCentre.text = uLevelName
                                binding.txtFirstPoints.text = "$points"
                                binding.txtFirstRank.text = "$resourceRank"
                                getResourcePic(uResourceId.toString(), binding.imgFirst)
                            }
                            if (resourceRank == 2){
                                binding.txtSecondName.text = resourceName
                                binding.txtSecondCentre.text = uLevelName
                                binding.txtSecondPoints.text = "$points"
                                binding.txtSecondRank.text = "$resourceRank"
                                getResourcePic(uResourceId.toString(), binding.imgSecond)
                            }
                            if (resourceRank == 3){
                                binding.txtThirdName.text = resourceName
                                binding.txtThirdCentre.text = uLevelName
                                binding.txtThirdPoints.text = "$points"
                                binding.txtThirdRank.text = "$resourceRank"
                                getResourcePic(uResourceId.toString(), binding.imgThird)
                            }
                            if (uResourceId == resourceId){
                                //binding.nameTv.text = resourceName
                                myPoints = points.toString().toInt()
                                //binding.pointsTv.text = "$myPoints"
                                //binding.rankTv.text = "$resourceRank"
                            }
                            pointsArr.add(points.toString().toInt())
                            quizLBResourceList.add(QuizLBResourceList(uLevelName, uResourceId, resourceName, isSelf, resourceRank, points, lastPlayedOn, lastPlayedOnDisplay, highest))
                        }
                        quizLBData.add(QuizLBData(levelName, quizLBResourceList))
                    }
                    highest = pointsArr.maxOrNull().toString().toInt()
                    QuizLeaderBoardBase(response.isSuccess, quizLBData, response.errorMessage, response.param1, response.responseMessage)
                }
                if (quizLBResourceList.isNotEmpty()){
                    for (i in 0 until quizLBResourceList.size) {
                        quizLBResourceList[i].highest = highest
                    }
                }
                quizLeaderBoardAdapter.notifyDataSetChanged()
            }

            override fun onFailure(requestCode: Int) {
            }
        })
        controller.getPuzzleLeaderBoard(202124, resourceId)
    }

    private fun getResourcePic(resourceId: String, iv: CircleImageView){
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<Any?> {
            override fun onResponse(requestCode: Int, response: Any?) {
                val base64 = response as String
                val decodedString = Base64.decode(base64, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                if (base64.length > 0) {
                    AppUtils.getResourceImage(resourceId, iv)
                    //binding.profileIv.setImageBitmap(decodedByte)
                }
            }
            override fun onFailure(requestCode: Int) {}
        })
        controller.getResourceProfilePicture(202129, resourceId)
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