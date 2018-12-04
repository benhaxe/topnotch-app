package com.dscfuta.topnotch.model

data class UserRequest(
        val email : String,
        val event_location: String,
        val event_type : String,
        val name: String,
        val phone: String,
        val service_type: List<String>
)  {

    constructor(): this("", "", "", "", "", listOf<String>())
}

data class FullRequest(
    val userRequest: UserRequest,
    val id: String
){
    constructor(): this(UserRequest("", "", "", "", "", listOf<String>()), "")
}