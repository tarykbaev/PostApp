package com.turar.arykbaev.postapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turar.arykbaev.postapp.R
import com.turar.arykbaev.postapp.models.Comment
import kotlinx.android.synthetic.main.layout_comment_list_item.view.*

class CommentRecyclerAdapter() : RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder>() {

    private var items: List<Comment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_comment_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitComment(commentList: List<Comment>) {
        items = commentList
        notifyDataSetChanged()
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.comment_name
        private val email: TextView = itemView.comment_email
        private val body: TextView = itemView.comment_body

        @SuppressLint("SetTextI18n")
        fun bind(comment: Comment) {
            name.text = "Name: ${comment.name}"
            email.text = "Email: ${comment.email}"
            body.text = "Comment: ${comment.body}"
        }
    }

}