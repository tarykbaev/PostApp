package com.turar.arykbaev.postapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turar.arykbaev.postapp.models.Post
import com.turar.arykbaev.postapp.R
import kotlinx.android.synthetic.main.layout_post_list_item.view.*

class PostRecyclerAdapter(var clickListener: OnPostItemClickListener) :
    RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {

    private var items: List<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_post_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    fun submitPost(postList: List<Post>) {
        items = postList
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userId: TextView = itemView.user_id
        private val postId: TextView = itemView.post_id
        private val title: TextView = itemView.post_title
        private val body: TextView = itemView.post_body

        @SuppressLint("SetTextI18n")
        fun bind(post: Post, action: OnPostItemClickListener) {
            postId.text = "Post Id: ${post.id}"
            userId.text = "User Id: ${post.userId}"
            title.text = "Title: ${post.title}"
            body.text = "Text: ${post.body}"

            itemView.setOnClickListener {
                action.onItemClick(post)
            }
        }
    }

    interface OnPostItemClickListener {
        fun onItemClick(item: Post)
    }
}