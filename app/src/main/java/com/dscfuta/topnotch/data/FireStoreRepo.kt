package com.dscfuta.topnotch.data

import android.content.Context
import com.dscfuta.topnotch.R
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class FireStoreRepo(val context: Context) {

    private val fireStore = FirebaseFirestore.getInstance()

    //Retrieves firestore instance
    fun getFireStoreInstance() = fireStore

    //Retrieves the the requests
    fun getUserRequestCollection() = fireStore.collection(context.getString(R.string.users_request_path))
}