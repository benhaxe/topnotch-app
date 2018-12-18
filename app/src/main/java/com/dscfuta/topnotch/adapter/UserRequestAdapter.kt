package com.dscfuta.topnotch.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.helpers.LabelHelper
import com.dscfuta.topnotch.databinding.ItemUserRequestBinding
import com.dscfuta.topnotch.helpers.OnUserRequestItemClickListener
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

        /*//Initializing the adapter of the service type
        val serviceTypeList = userRequestList[position].userRequest.service_type
        Log.d(TAG, "My Service type: $serviceTypeList")*/

        //Make Label
        val labelText = userRequestList[position].userRequest.name[0].toString().toUpperCase()

        /**Handling a very long name*/
        binding.apply {
            if(userRequestList[position].userRequest.name.length < 30){
                itemUserRequestUserName.text = userRequestList[position].userRequest.name
            }else{
                val shortenedName = userRequestList[position].userRequest.name.substring(0, 30) + "..."
                itemUserRequestUserName.text = shortenedName
            }
            /**Making the label work with style*/
            itemUserRequestLabel.text = labelText
            itemUserRequestLabel.backgroundDrawable = LabelHelper.setDrawableFromText(labelText, context)

            itemUserRequestEventDate.text = userRequestList[position].userRequest.event_date
        }

        val id = userRequestList[position].id

        /*//Delete Button Function
        binding.deleteRequestButton.setOnClickListener {

        try{

            val deleteClickListener = context as OnDeleteRequestButtonClickListener
                deleteClickListener.onDeleteButtonClicked(id)

        }catch (e: Exception){

                Toast.makeText(context, "Exception: Fragment/Activity class must implement OnUserRequestItemClickListener", Toast.LENGTH_LONG)
                        .show()
                Log.e(UserRequestAdapter::class.java.simpleName, "Exception: Fragment/Activity class must implement OnUserRequestItemClickListener")

            }
        }*/


        //Breaking down each userRequest for fragment parsing
        val currentUserRequest =  userRequestList[position].userRequest

        val name = currentUserRequest.name
        val emailAddress = currentUserRequest.email
        val eventLocation = currentUserRequest.event_location
        val phoneNumber = currentUserRequest.phone
        val eventType = currentUserRequest.event_type
        val eventDate = currentUserRequest.event_date
        val serviceType = currentUserRequest.service_type

        //Registering the listener
        holder.itemView.setOnClickListener {
            try{
                val clickListener  = context as OnUserRequestItemClickListener

                //Used named arguments to make the parameter passed clearer
                clickListener.onUserRequestItemClick(
                        it,
                        name = name,
                        phoneNumber = phoneNumber,
                        eventLocation = eventLocation,
                        eventType = eventType,
                        email = emailAddress,
                        eventDate = eventDate,
                        mService = serviceType,
                        id = id
                )

            }catch (e: Exception){
                //Just to be safe
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                        .show()
                Log.e(UserRequestAdapter::class.java.simpleName, "Exception: Fragment/Activity class must implement OnUserRequestItemClickListener")

            }


        }
    }
}

class UserRequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
