package com.dscfuta.topnotch.data

import androidx.lifecycle.ViewModel

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class RequestsViewModel: ViewModel() {

    val fireStoreRepo = FireStoreRepo()
    val requestsCollection = fireStoreRepo.getUserRequestCollection()

    fun getRequests(body : (exception: String) -> Unit) = DocumentLiveData(requestsCollection, body)

    fun deleteRequest(
            path: String,
            if_successful : () -> Unit,
            if_not_successful: (exception: String) -> Unit) = fireStoreRepo.deleteRequest(
            path = path,
            ifNotSuccessful = if_not_successful,
            ifSuccessful = if_successful
    )

}