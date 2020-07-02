package com.turar.arykbaev.postapp.api

import com.turar.arykbaev.postapp.models.Comment
import com.turar.arykbaev.postapp.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceHolderApi {
    @GET("/posts")
    fun getPost(): Call<List<Post>>

    @GET("/comments")
    fun getComment(@Query("postId") postId: Int): Call<List<Comment>>
}