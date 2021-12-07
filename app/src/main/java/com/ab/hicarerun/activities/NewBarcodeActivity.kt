package com.ab.hicarerun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityNewBarcodeBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class NewBarcodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewBarcodeBinding
    lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBarcodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        codeScanner = CodeScanner(this, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false
        binding.submitBtn.setOnClickListener {
            val customText = binding.customEt.text.toString().trim()
            if (customText != "") {
                val intent = Intent()
                intent.putExtra("content", customText)
                setResult(3000, intent)
                finish()
            }else{
                Toast.makeText(this, "Please enter code", Toast.LENGTH_SHORT).show()
            }
        }

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                val intent = Intent()
                intent.putExtra("content", it.text)
                setResult(3000, intent)
                finish()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}