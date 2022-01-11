package com.ab.hicarerun.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ab.hicarerun.R
import com.ab.hicarerun.network.models.tsscannermodel.Option_List

class CustomSpinnerAdapter(context: Context, resource: Int, val optionList: List<Option_List>) : ArrayAdapter<Option_List>(context, resource) {

    override fun getCount(): Int {
        return optionList.size
    }

    override fun getItem(position: Int): Option_List {
        return optionList[position]
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun setDropDownViewResource(resource: Int) {
        super.setDropDownViewResource(resource)
        R.layout.spinner_popup
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0){
            tv.setTextColor(Color.GRAY)
        }else{
            tv.setTextColor(Color.BLACK)
        }
        return view
    }
}