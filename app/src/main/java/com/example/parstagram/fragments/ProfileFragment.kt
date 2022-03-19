package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

//Extending the FeedFragment class after making it "open"
class ProfileFragment : FeedFragment() {
    //Overriding what queryPost is doing
    override fun queryPosts(){
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Telling parse to find all the post objects and return it to us
        query.include(Post.KEY_USER)
        //Only returning posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        //So that we have the newer posts
        query.addDescendingOrder("createdAt")

        //Returning only recent 20 post
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                //If the exception is not null then...
                if ( e !=  null){
                    //Something went wrong
                    Log.e(MainActivity.TAG, "Error fetching posts")
                }else{
                    if(posts != null){
                        for ( post in posts){
                            Log.i(TAG, "Post: "+ post.getDescription() + " , username: " +
                                    post.getUser()?.username)
                        }
                        //Added after setting up the adapter
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

}