package com.dscfuta.topnotch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscfuta.topnotch.R

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class ServiceTypeAdapter(
        val serviceTypeList: List<String>,
        val context: Context
): RecyclerView.Adapter<ServiceTypeAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sevice_type_layout, parent, false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return serviceTypeList.size
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val textView = holder.itemView as TextView
        textView.text = serviceTypeList[position]
    }

    class ServiceViewHolder(v: View): RecyclerView.ViewHolder(v)

}