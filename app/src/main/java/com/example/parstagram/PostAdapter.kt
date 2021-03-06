package com.example.parstagram

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

///When we are creating an adapter, we need 2 things
//1. context
//2. list of posts
class PostAdapter(val context: Context, val posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    //For pull to refresh (changed the list to mutableList
    // Clean all elements of the recycler
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }
    //For pull to refresh
    // Add a list of items -- change to type used
    fun addAll(posts2: List<Post>) {
        posts.addAll(posts2)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        //Specify the layout file to use for this item

        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvUsername : TextView
        val ivImage : ImageView
        val tvDescription : TextView

        init{
            tvUsername = itemView.findViewById(R.id.tvUserName)
            ivImage = itemView.findViewById(R.id.ivImage)
            tvDescription = itemView.findViewById(R.id.tvDescription)
        }

        //Will set the views
        fun bind(post : Post){
            tvDescription.text = post.getDescription()
            tvUsername.text = post.getUser()?.username

            //populate image (we will use the glide memory)
            //Taking image from the post and loading it into our layout
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)

        }
    }

}