package com.ab.hicarerun.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowBarcodeItemBinding
import com.ab.hicarerun.handler.OnBarcodeCountListener
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.tsscannermodel.BarcodeList
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


class BarcodeAdapter(
    val context: Context,
    val barcodeList: ArrayList<BarcodeList>,
    private val comingFrom: String
) : RecyclerView.Adapter<BarcodeAdapter.MyHolder>(), LocationManagerListner {
    var lat = "0.0"
    var long = "0.0"
    private var onBarcodeCountListener: OnBarcodeCountListener? = null
    fun setOnBarcodeCountListener(l: OnBarcodeCountListener) {
        onBarcodeCountListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view, comingFrom)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, barcodeList[position])
        holder.binding.deleteBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Delete")
            dialog.setMessage("Are you sure you want to delete the equipment?")
            dialog.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                if (barcodeList[position].id != 0) {
                    removeFromServer(position)
                } else {
                    removeAt(position)
                }
            }
            dialog.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

    fun removeFromServer(position: Int) {
        val verifyMap = HashMap<String, Any?>()
        verifyMap["BarcodeId"] = barcodeList[position].id
        verifyMap["ActivityName"] = "Deleted"
        verifyMap["Account_No"] = barcodeList[position].account_No
        verifyMap["Order_No"] = barcodeList[position].order_No
        verifyMap["Barcode_Data"] = barcodeList[position].barcode_Data
        verifyMap["Lat"] = lat
        verifyMap["Long"] = long
        verifyMap["VerifiedOn"] = ""
        verifyMap["VerifiedBy"] = ""

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse> {
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null) {
                    if (response.isSuccess == true) {
                        if (response.data == "Deleted") {
                            removeAt(position)
                        }
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
            }
        })
        controller.deleteBarcodeDetails(20213, verifyMap)
    }

    fun removeAt(position: Int) {
        barcodeList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, barcodeList.size)
        onBarcodeCountListener?.onBarcodeCountListener(barcodeList.size)
        Toast.makeText(context, "Barcode is deleted", Toast.LENGTH_SHORT).show()
    }

    class MyHolder(val binding: RowBarcodeItemBinding, private val comingFrom: String) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(context: Context, barcodeList: BarcodeList) {
            val barcode_data = barcodeList.barcode_Data.toString()
            val isVerified = barcodeList.isVerified
            val id = barcodeList.id
            binding.naBtn.visibility = View.GONE
            if (barcodeList.service_Unit != null && barcodeList.service_Unit != "") {
                binding.serviceUnitTv.text = barcodeList.service_Unit
            }else{
                binding.serviceUnitTv.text = "N/A"
            }
            if (comingFrom == "TSScanner") {
                binding.equipmentTypeLayout.visibility = View.VISIBLE
                binding.serviceUnitLayout.visibility = View.VISIBLE
                binding.equipmentTypeTv.text = barcodeList.barcode_Type
                binding.isBarcodeVerified.visibility = View.GONE
                binding.verifiedOnLayout.visibility = View.GONE
                binding.deleteBtn.visibility = View.VISIBLE
            } else {
                binding.deleteBtn.visibility = View.GONE
                binding.isBarcodeVerified.visibility = View.VISIBLE
                binding.verifiedOnLayout.visibility = View.VISIBLE
            }
            if (barcodeList.callForDelete == "yes") {
                binding.dataCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_red_100
                    )
                )
            } else {
                binding.dataCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
            if (barcodeList.additional_Info != null && barcodeList.additional_Info != ""){
                binding.remarksTv.text = barcodeList.additional_Info
                binding.remarksLayout.visibility = View.VISIBLE
            }else{
                binding.remarksLayout.visibility = View.GONE
            }
            if (isVerified == true) {
                val verifiedOn = barcodeList.last_Verified_On
                val time = AppUtils.reFormatDateTime(verifiedOn, "HH:mm")
                val date = AppUtils.reFormatDateTime(verifiedOn, "MMM dd, yyyy")

                binding.verifiedOnTv.text = "At $time on $date"
                //binding.verifiedOnTv.text = verifiedOn?.substring(0, 10)
                binding.isBarcodeVerified.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_check_circle_green
                    )
                )
            } else {
                binding.verifiedOnTv.text = "N/A"
                binding.isBarcodeVerified.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_check_circle_black
                    )
                )
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
            bitMatrix = try {
                MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, null)
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
            val bitmap =
                Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight)
            return bitmap
        }
        /*private fun displayBitmap(context: Context, value: String) {
            val widthPixels = 25
            val heightPixels = 25

            binding.barcodeIv.setImageBitmap(
                createBarcodeBitmap(
                    barcodeValue = value,
                    barcodeColor = ContextCompat.getColor(context, R.color.colorPrimary),
                    backgroundColor = ContextCompat.getColor(context, android.R.color.white),
                    widthPixels = widthPixels,
                    heightPixels = heightPixels
                )
            )
        }

        private fun createBarcodeBitmap(
            barcodeValue: String,
            @ColorInt barcodeColor: Int,
            @ColorInt backgroundColor: Int,
            widthPixels: Int,
            heightPixels: Int
        ): Bitmap {
            val bitMatrix = Code128Writer().encode(
                barcodeValue,
                BarcodeFormat.CODE_128,
                widthPixels,
                heightPixels
            )

            val pixels = IntArray(bitMatrix.width * bitMatrix.height)
            for (y in 0 until bitMatrix.height) {
                val offset = y * bitMatrix.width
                for (x in 0 until bitMatrix.width) {
                    pixels[offset + x] =
                        if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
                }
            }

            val bitmap = Bitmap.createBitmap(
                bitMatrix.width,
                bitMatrix.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(
                pixels,
                0,
                bitMatrix.width,
                0,
                0,
                bitMatrix.width,
                bitMatrix.height
            )
            return bitmap
        }*/
    }

    override fun locationFetched(
        mLocation: Location?,
        oldLocation: Location?,
        time: String?,
        locationProvider: String?
    ) {
        lat = mLocation?.latitude.toString()
        long = mLocation?.longitude.toString()
    }
}