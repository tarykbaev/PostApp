package com.turar.arykbaev.postapp.models


import com.google.gson.annotations.SerializedName

data class Comment (
    @SerializedName("body")
    val body: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
)


