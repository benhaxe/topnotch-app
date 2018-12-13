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

    override fun onInactive() {
        super.onInactive()

    }

    override fun onActive() {
        super.onActive()

        collectionReference.addSnapshotListener{
            doc, exception ->
            val added_list = mutableListOf<FullRequest>()
            val removed_list = mutableListOf<FullRequest>()
                if(doc != null){
                    for(document in doc.documentChanges) {

                        //Checking if a document is added
                        //This should also be triggeref=d if a document is deleted. I hope

                        if(document.type == DocumentChange.Type.ADDED){
                            val userRequest = document.document.toObject(UserRequest::class.java)
                            val id = document.document.id

                            added_list.add(FullRequest(userRequest, id))
                            value = added_list

                        }

                }

            }

            //Using said function with the message
            if (exception != null){
                body(exception.localizedMessage)
            }

            //The value to be observed


        }
    }
}