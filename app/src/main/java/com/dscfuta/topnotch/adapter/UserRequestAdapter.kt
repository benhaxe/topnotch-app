package com.dscfuta.topnotch.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.Utils
import com.dscfuta.topnotch.databinding.ItemUserRequestBinding
import com.dscfuta.topnotch.model.FullRequest
import org.jetbrains.anko.backgroundDrawable

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

        //Storing the id of the request to be deleted..
        holder.itemView.tag = userRequestList[position].id

        //Initializing the adapter of the service type
        val serviceTypeList = userRequestList[position].userRequest.service_type
        val adapter = ServiceTypeAdapter(serviceTypeList, context)

        //Still Initializing
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        //In case if user types in their name in lowercase
        val labelText = userRequestList[position].userRequest.name[0].toString().toUpperCase()

        //Setting data to the respective views
        binding.itemUserRequestEventTypesRecycler.setHasFixedSize(true)
        binding.itemUserRequestEventTypesRecycler.layoutManager = manager
        binding.itemUserRequestEventTypesRecycler.adapter = adapter

        binding.apply {
            itemUserRequestUserName.text = userRequestList[position].userRequest.name

            itemUserRequestLabel.text = labelText
            itemUserRequestLabel.backgroundDrawable = Utils.setColorFromText(labelText, context)
        }

        //Breaking down each userRequest for fragment passing
        val currentUserRequest =  userRequestList[position].userRequest

        val id = userRequestList[position].id
        val name = currentUserRequest.name
        val emailAddress = currentUserRequest.email
        val eventLocation = currentUserRequest.event_location
        val phoneNumber = currentUserRequest.phone
        val eventType = currentUserRequest.event_type
        val serviceType = currentUserRequest.service_type

        //Registering the listener
        holder.itemView.setOnClickListener {
            try {
                val clickListener  = context as onUserRequestItemClicListener

                //Used named arguments to make the parameter passed clearer
                clickListener.onUserRequestItemClick(it, name = name, phoneNumber = phoneNumber, eventLocation = eventLocation, eventType = eventType,
                        email = emailAddress, serviceType = serviceType, id = id
                        )

                //Just to be safe
            }catch (e: Exception){

                Toast.makeText(context, "Exception: Fragment/Activity class must implement OnUserRequestItemClickListener", Toast.LENGTH_LONG)
                        .show()
                Log.e(UserRequestAdapter::class.java.simpleName, "Exception: Fragment/Activity class must implement OnUserRequestItemClickListener")
            }
        }
    }
}

class UserRequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface onUserRequestItemClicListener{
    fun onUserRequestItemClick(v : View, name: String, phoneNumber: String, eventLocation: String, eventType: String, email: String, serviceType: List<String>, id: String)
}
