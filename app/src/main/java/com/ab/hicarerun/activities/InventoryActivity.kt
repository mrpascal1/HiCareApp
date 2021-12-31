package com.ab.hicarerun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityInventoryBinding

class InventoryActivity : BaseActivity() {
    lateinit var binding: ActivityInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backIv.setOnClickListener {
            getBack()
        }
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