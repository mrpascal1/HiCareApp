package com.ab.hicarerun.adapter

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowBarcodeItemBinding
import com.ab.hicarerun.handler.OnBarcodeCountListener
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDetailsData
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class BarcodeAdapter2(val context: Context, val barcodeList: ArrayList<BarcodeDetailsData>, private val comingFrom: String) : RecyclerView.Adapter<BarcodeAdapter2.MyHolder>(), LocationManagerListner {
    var lat = "0.0"
    var long = "0.0"
    private var onBarcodeCountListener: OnBarcodeCountListener? = null
    private var notAccessibleListener: NotAccessibleListener? = null
    fun setOnBarcodeCountListener(l: OnBarcodeCountListener){
        onBarcodeCountListener = l
    }
    fun setOnNAClickListener(notAccessibleListener: NotAccessibleListener){
        this.notAccessibleListener = notAccessibleListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view, comingFrom)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, barcodeList[position])
        holder.binding.naBtn.setOnClickListener {
            notAccessibleListener?.onNAClicked(barcodeList[position].barcode_Data.toString())
        }
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

    class MyHolder(val binding: RowBarcodeItemBinding, private val comingFrom: String): RecyclerView.ViewHolder(binding.root){
        fun bindItems(context: Context, barcodeList: BarcodeDetailsData){
            val barcode_data = barcodeList.barcode_Data.toString()
            val isVerified = barcodeList.isVerified
            val id = barcodeList.id
            binding.equipmentTypeTv.text = barcodeList.barcode_Type
            binding.verifiedOnLayout.visibility = View.GONE
            if (comingFrom == "TSScanner"){
                binding.equipmentTypeLayout.visibility = View.GONE
                binding.isBarcodeVerified.visibility = View.GONE
                //binding.verifiedOnLayout.visibility = View.GONE
                binding.deleteBtn.visibility = View.VISIBLE
                binding.naBtn.visibility = View.GONE
            }else{
                binding.equipmentTypeLayout.visibility = View.VISIBLE
                binding.deleteBtn.visibility = View.GONE
                binding.isBarcodeVerified.visibility = View.VISIBLE
                //binding.verifiedOnLayout.visibility = View.VISIBLE
                binding.naBtn.visibility = View.VISIBLE
            }
            /*if (barcodeList.callForDelete == "yes"){
                binding.dataCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_red_100))
            }else{
                binding.dataCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }*/
            if (isVerified == true){
                val verifiedOn = barcodeList.last_Verified_On
                Log.d("TAG-Adapt", verifiedOn.toString())
                val time = AppUtils.reFormatDateTime(verifiedOn, "HH:mm")
                val date = AppUtils.reFormatDateTime(verifiedOn, "MMM dd, yyyy")

                binding.verifiedOnTv.text = "At $time on $date"
                //binding.verifiedOnTv.text = verifiedOn?.substring(0, 10)
                binding.naBtn.visibility = View.GONE
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green))
            }else{
                binding.verifiedOnTv.text = "N/A"
                binding.naBtn.visibility = View.VISIBLE
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black))
            }
            /*if (id == 0){
                binding.deleteBtn.visibility = View.VISIBLE
            }else{
                binding.deleteBtn.visibility = View.GONE
            }*/
            binding.barcodeIv.setImageBitmap(textToImage(barcode_data, 300, 300))
            //displayBitmap(context, barcode_data)
            binding.barcodeTv.text = barcode_data
            Log.d("TAG-Adapter", barcode_data)
        }

        @Throws(WriterException::class, NullPointerException::class)
        private fun textToImage(text: String, width: Int, height: Int): Bitmap? {
            val bitMatrix: BitMatrix
            bitMatrix = try { MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, null)
            } catch (Illegalargumentexception: IllegalArgumentException) {
                return null
            }
            val bitMatrixWidth = bitMatrix.width
            val bitMatrixHeight = bitMatrix.height
            val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
            val colorWhite = -0x1
            val colorBlack = -0x1000000
            for (y in 0 until bitMatrixHeight) {
                val offset = y * bitMatrixWidth
                for (x in 0 until bitMatrixWidth) {
                    pixels[offset + x] = if (bitMatrix[x, y]) colorBlack else colorWhite
                }
            }
            val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight)
            return bitmap
        }
    }


    interface NotAccessibleListener{
        fun onNAClicked(barcodeData: String)
    }

    override fun locationFetched(mLocation: Location?, oldLocation: Location?, time: String?, locationProvider: String?) {
        lat = mLocation?.latitude.toString()
        long = mLocation?.longitude.toString()
    }
}