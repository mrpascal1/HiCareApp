package com.ab.hicarerun.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.RowBarcodeItemBinding
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeList
import com.ab.hicarerun.utils.AppUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer


class BarcodeAdapter(val context: Context, val barcodeList: ArrayList<BarcodeList>, private val comingFrom: String) : RecyclerView.Adapter<BarcodeAdapter.MyHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = RowBarcodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view, comingFrom)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, barcodeList[position])
        holder.binding.deleteBtn.setOnClickListener {
            removeAt(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

    fun removeAt(position: Int){
        barcodeList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, barcodeList.size)
    }

    class MyHolder(val binding: RowBarcodeItemBinding, private val comingFrom: String): RecyclerView.ViewHolder(binding.root){
        fun bindItems(context: Context, barcodeList: BarcodeList){
            val barcode_data = barcodeList.barcode_Data.toString()
            val isVerified = barcodeList.isVerified
            val verifiedOn = barcodeList.last_Verified_On
            val id = barcodeList.id
            if (comingFrom == "TSScanner"){
                binding.isBarcodeVerified.visibility = View.GONE
                binding.verifiedOnLayout.visibility = View.GONE
            }
            if (isVerified == true){
                binding.verifiedOnTv.text = verifiedOn?.substring(0, 10)
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green))
            }else{
                binding.verifiedOnTv.text = "N/A"
                binding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black))
            }
            if (id == 0){
                binding.deleteBtn.visibility = View.VISIBLE
            }else{
                binding.deleteBtn.visibility = View.GONE
            }
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
}