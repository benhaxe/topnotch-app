package com.dscfuta.topnotch.data

import androidx.lifecycle.LiveData
import com.dscfuta.topnotch.model.FullRequest
import com.dscfuta.topnotch.model.UserRequest
import com.google.firebase.firestore.CollectionReference

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class DocumentLiveData(val collectionReference: CollectionReference,
        //Function to be executed if something goes wrong
                       val body: (exception: String) -> Unit): LiveData<List<FullRequest>>() {


    override fun onActive() {
        super.onActive()

        collectionReference.addSnapshotListener{ doc, exception ->
            val added_list = mutableListOf<FullRequest>()

            /**Checking if document is empty or not*/
            if(doc != null && !doc.isEmpty){
                for(document in doc.documents){
                    val userRequests  = document.toObject(UserRequest::class.java) /**Convert the document to Object*/
                    val id = document.id //Get [ID] of the document

                    if(userRequests != null) added_list.add(FullRequest(userRequests, id))

                }

                /**
                 * [Value] Referring to value of [LiveDate]
                 * We will add this list to the list wrapped in our live data
                 * */
                value = added_list

            }

            //Using said function with the message
            if (exception != null){
                body(exception.localizedMessage)
            }

        }

    }
}
