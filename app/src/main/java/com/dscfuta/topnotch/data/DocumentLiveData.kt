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
            val list = mutableListOf<FullRequest>()
                if(doc != null){
                    for(document in doc.documentChanges) {

                        //Checking if a document is added or removed
                        if(document.type == DocumentChange.Type.ADDED || document.type == DocumentChange.Type.REMOVED){
                            val userRequest = document.document.toObject(UserRequest::class.java)
                            val id = document.document.id

                            list.add(FullRequest(userRequest, id))
                        }

                }
            }

            //Using said function with the message
            if (exception != null){
                body(exception.localizedMessage)
            }

            //The value to be observed
            value = list

        }
    }
}