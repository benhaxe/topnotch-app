package com.dscfuta.topnotch.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dscfuta.topnotch.R


class FullDetails : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_details, container, false)
    }


    /**
     * Icon Actions for opening Implicit Intent and parsing in particular
     * parameter based on opening application type
     * */

    //Make Call
    fun makeCall(number : String){
        //TODO Create Implicit intent to open [Call Dialer] app and parse in the phone number, then make call
    }

    fun sendAMail(email : String){
        //TODO Create Implicit intent to open [Mailing] app(s) and parse in the email address in the to field
    }

    fun sendTextMessage(text : String){
        //TODO Create Implicit intent to open [Text Message App] app and parse in the phone number
    }

    fun chatOnWhatsapp(text : String){
        //TODO Create Implicit intent to open [Whatsapp App] app and parse in the phone number, so the owner can chat with this users
    }

    /**Perform clicking Options*/
    override fun onClick(v: View?) {
        //TODO use [when] check for the id being clicked and call the neccessary [Icon Action] method
    }
}
