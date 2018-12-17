package com.dscfuta.topnotch.data

import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class FireStoreRepo{

    private val fireStore = FirebaseFirestore.getInstance()

    //Retrieves the the requests
    fun getUserRequestCollection() = fireStore.collection("Users Request")

    fun deleteRequest(
            path: String,
            ifSuccessful: () -> Unit,
            ifNotSuccessful: (exception: String) -> Unit){
        getUserRequestCollection().document(path).delete().addOnCompleteListener {
            if(it.isSuccessful){
                ifSuccessful()
            }
            else{
                ifNotSuccessful(it.exception!!.localizedMessage)
            }
        }
    }
}