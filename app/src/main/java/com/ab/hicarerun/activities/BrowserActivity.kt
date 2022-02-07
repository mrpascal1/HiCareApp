package com.ab.hicarerun.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.ab.hicarerun.databinding.ActivityBrowserBinding
import com.ab.hicarerun.utils.AppUtils


class BrowserActivity : AppCompatActivity() {
    lateinit var binding: ActivityBrowserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val orderNo = intent.getStringExtra("orderNo").toString()
        val sequenceNo = intent.getStringExtra("sequenceNo").toString()

        binding.progressBar.max = 100
        binding.webView.loadUrl("http://connect.hicare.in/b2bwow_webuat/ServiceUnit/UpdateFromRun?OrderNo=$orderNo&user=${AppUtils.resourceId}&sequenceNo=$sequenceNo&userProfile=Technician")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.progress = newProgress
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                binding.titleTv.text = title
            }

            override fun onReceivedIcon(view: WebView, icon: Bitmap) {
                super.onReceivedIcon(view, icon)
            }
        }
        binding.backIv.setOnClickListener {
            finish()
        }
    }
}