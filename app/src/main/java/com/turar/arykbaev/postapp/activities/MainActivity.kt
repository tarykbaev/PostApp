package com.turar.arykbaev.postapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.turar.arykbaev.postapp.R
import com.turar.arykbaev.postapp.adapters.PostRecyclerAdapter
import com.turar.arykbaev.postapp.api.JsonPlaceHolderApi
import com.turar.arykbaev.postapp.models.Post
import com.turar.arykbaev.postapp.utils.Util
import com.turar.arykbaev.postapp.utils.Util.Companion.progressBarStart
import com.turar.arykbaev.postapp.utils.Util.Companion.progressBarStop
import com.turar.arykbaev.postapp.utils.Util.Companion.showSnackBar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity(), PostRecyclerAdapter.OnPostItemClickListener {

    private lateinit var postAdapter: PostRecyclerAdapter
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBarStart(main_progress_bar)
        setActionBar()
        initRecyclerView()
        initRetrofit()
        jsonPlaceHolderApi()
    }

    private fun setActionBar() {
        setSupportActionBar(main_toolbar)
    }

    private fun initRecyclerView() {
        postAdapter = PostRecyclerAdapter(this)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }
    }

    private fun initRetrofit() {
        retrofit = Util.getRetrofit()
    }

    private fun jsonPlaceHolderApi() {
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call = jsonPlaceHolderApi.getPost()

        call.enqueue(object : Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                if (!response.isSuccessful) {
                    showSnackBar(main_parent, "Oops! Something went wrong.")
                    progressBarStop(main_progress_bar)
                    return
                }

                val posts = response.body()

                posts?.let {
                    if (posts.size > 30)
                        postAdapter.submitPost(posts.subList(0, 30))
                    else
                        postAdapter.submitPost(posts)
                }

                progressBarStop(main_progress_bar)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                progressBarStart(main_progress_bar)
                showSnackBar(main_parent, "Oops! Something went wrong.")
            }
        })
    }

    override fun onItemClick(item: Post) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("ID", item.id)
        intent.putExtra("TITLE", item.title)
        intent.putExtra("BODY", item.body)
        startActivity(intent)
    }
}