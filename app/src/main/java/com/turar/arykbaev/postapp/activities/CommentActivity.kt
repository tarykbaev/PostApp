package com.turar.arykbaev.postapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.turar.arykbaev.postapp.R
import com.turar.arykbaev.postapp.adapters.CommentRecyclerAdapter
import com.turar.arykbaev.postapp.api.JsonPlaceHolderApi
import com.turar.arykbaev.postapp.models.Comment
import com.turar.arykbaev.postapp.utils.Util
import com.turar.arykbaev.postapp.utils.Util.Companion.progressBarStart
import com.turar.arykbaev.postapp.utils.Util.Companion.progressBarStop
import com.turar.arykbaev.postapp.utils.Util.Companion.showSnackBar
import kotlinx.android.synthetic.main.activity_comment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentActivity : AppCompatActivity() {

    private lateinit var commentAdapter: CommentRecyclerAdapter
    private lateinit var retrofit: Retrofit
    private var comments: MutableList<Comment>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        progressBarStart(comment_progress_bar)
        getExtrasAndInit()
        setActionBar()
        initRecyclerView()
        if (savedInstanceState == null) {
            initRetrofit()
            jsonPlaceHolderApi(intent.extras?.get("ID").toString().toInt())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getExtrasAndInit() {
        comment_post_id.text = "Post Id: ${intent.extras?.get("ID").toString()}"
        comment_post_title.text = "Title: ${intent.extras?.get("TITLE").toString()}"
        comment_post_body.text = "Text: ${intent.extras?.get("BODY").toString()}"
    }

    private fun setActionBar() {
        this.title = "Comments"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDisplayHomeAsUp()
    }

    private fun initRecyclerView() {
        commentAdapter = CommentRecyclerAdapter()
        comment_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CommentActivity)
            adapter = commentAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.comment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.comment_item) {
            showCommentDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setDisplayHomeAsUp() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRetrofit() {
        retrofit = Util.getRetrofit()
    }

    private fun jsonPlaceHolderApi(postId: Int) {
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call = jsonPlaceHolderApi.getComment(postId)

        call.enqueue(object : Callback<List<Comment>> {

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {

                if (!response.isSuccessful) {
                    showSnackBar(comment_parent, "Oops! Something went wrong.")
                    progressBarStop(comment_progress_bar)
                    return
                }

                comments = response.body() as MutableList<Comment>?

                comments?.let { commentAdapter.submitComment(it) }

                progressBarStop(comment_progress_bar)
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                showSnackBar(comment_parent, "Oops! Something went wrong.")
                progressBarStop(comment_progress_bar)
            }
        })
    }

    private fun showCommentDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.layout_add_comment)

        dialog.findViewById<TextView>(R.id.positive_button).setOnClickListener {

            val dialogComment =
                dialog.getCustomView().findViewById<TextInputEditText>(R.id.dialog_comment)
            val dialogEmail =
                dialog.getCustomView().findViewById<TextInputEditText>(R.id.dialog_email)
            val dialogName =
                dialog.getCustomView().findViewById<TextInputEditText>(R.id.dialog_name)

            if (commentValidation(dialogComment, dialogEmail, dialogName)) {

                addComment(
                    dialogComment.text.toString(),
                    dialogEmail.text.toString(),
                    dialogName.text.toString()
                )

                showSnackBar(comment_parent, "Comment added")

                dialog.dismiss()
            }
        }

        dialog.findViewById<TextView>(R.id.negative_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addComment(comment: String, email: String, name: String) {

        val item = Comment(comment, email, name)

        comments?.add(item)
        comments?.let { it1 -> commentAdapter.submitComment(it1) }
    }

    private fun commentValidation(
        comment: TextInputEditText,
        email: TextInputEditText,
        name: TextInputEditText
    ): Boolean {
        when {
            TextUtils.isEmpty(name.text) -> {
                name.error = "Can't be empty"
                return false
            }
            TextUtils.isEmpty(email.text) -> {
                email.error = "Can't be empty"
                return false
            }
            TextUtils.isEmpty(comment.text) -> {
                comment.error = "Can't be empty"
                return false
            }
            !emailValidation(email.text.toString()) -> {
                email.error = "Invalid email"
                return false
            }
        }
        return true
    }

    private fun emailValidation(email: String): Boolean {
        val pattern: Pattern = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        )
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val json = Gson().toJson(comments)
        outState.putString("comment_key", json)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val type = object : TypeToken<ArrayList<Comment>>(){}.type
        val json = savedInstanceState.getString("comment_key")
        comments = Gson().fromJson(json, type)
        comments?.let { commentAdapter.submitComment(it) }
        progressBarStop(comment_progress_bar)
    }
}


