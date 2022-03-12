package com.example.parstagram
//import android.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseException
import com.parse.ParseObject

import com.parse.*

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

/**
 * Let user create a post by taking a photo with their camera
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryPosts()
    }

    //Query for all post in our server (not using get since that gets one object)
    fun queryPosts(){
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Telling parse to find all the post objects and return it to us
        query.include(Post.KEY_USER)
        query.findInBackground(object:FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                //If the exception is not null then...
                if ( e !=  null){
                    //Something went wrong
                    Log.e(TAG, "Error fetching posts")
                }else{
                    if(posts != null){
                        for ( post in posts){
                            Log.i(TAG, "Post: "+ post.getDescription() + " , username: " +
                            post.getUser()?.username)
                        }
                    }


                }
            }

        })
    }

    companion object{
        const val TAG = "MainActivity"
    }
}