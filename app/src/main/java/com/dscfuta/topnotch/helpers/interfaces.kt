package com.dscfuta.topnotch.helpers

import android.view.View

interface OnUserRequestItemClickListener{
    fun onUserRequestItemClick(
            v : View,
            name: String,
            phoneNumber: String,
            eventLocation: String,
            eventType: String,
            email: String,
            eventDate : String,
            mService: List<String>,
            id: String)
}

//To Detect when the delete button is clicked cleanly
interface OnDeleteRequestButtonClickListener{
    fun  onDeleteButtonClicked(id: String)
}