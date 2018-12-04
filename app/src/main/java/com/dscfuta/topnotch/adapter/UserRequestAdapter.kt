package com.dscfuta.topnotch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.databinding.ItemUserRequestBinding
import com.dscfuta.topnotch.model.FullRequest
import com.dscfuta.topnotch.model.UserRequest

class UserRequestAdapter(
        private val userRequestList: List<FullRequest>,
         val context : Context) : RecyclerView.Adapter<UserRequestHolder>() {

    //Initializing
    private lateinit var binding: ItemUserRequestBinding
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRequestHolder {
        binding= DataBindingUtil.inflate(inflater, R.layout.item_user_request, parent, false)
        return UserRequestHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return userRequestList.size
    }

    override fun onBindViewHolder(holder: UserRequestHolder, position: Int) {

        var eventTypes: String? = ""
        for (eventType in userRequestList[position].userRequest.service_type){
            eventTypes = "$eventTypes $eventType."
        }

        val finalEventType = if(eventTypes!!.length > 50){
            eventTypes.substring(0, 30) + "..."
        }else{
            eventTypes
        }


        //Setting data to the respective views
        binding.apply {
            itemUserRequestUserName.text = userRequestList[position].userRequest.name
            itemUserRequestLabel.text = userRequestList[position].userRequest.name[0].toString()
            itemUserRequestEventTypes.text = finalEventType
        }
    }
}

class UserRequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
