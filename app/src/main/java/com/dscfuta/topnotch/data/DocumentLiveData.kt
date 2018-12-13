package com.dscfuta.topnotch.data

import androidx.lifecycle.LiveData
import com.dscfuta.topnotch.model.FullRequest
import com.dscfuta.topnotch.model.UserRequest
import com.google.firebase.firestore.*

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class DocumentLiveData(val collectionReference: CollectionReference,
                       //Function to be executed if something goes wrong
                       val body: (excepption: String) -> Unit): LiveData<List<FullRequest>>() {


    override fun onActive() {
        super.onActive()

        collectionReference.addSnapshotListener{
            doc, exception ->
            val added_list = mutableListOf<FullRequest>()

                if(doc != null && !doc.isEmpty){
                    for(document in doc.documents){
                        val userReuqests  = document.toObject(UserRequest::class.java)
                        val id = document.id

                        if(userReuqests != null)added_list.add(FullRequest(userReuqests, id))

                    }

                    value = added_list

                }

            //Using said function with the message
            if (exception != null){
                body(exception.localizedMessage)
               }

            }

        }
    }
