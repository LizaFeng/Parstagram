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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

/**
 * Let user create a post by taking a photo with their camera
 */
class MainActivity : AppCompatActivity() {
    val APP_TAG = "MyCustomApp"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        //for the menu bars
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener{
            //Creates a variable for us called item, if we do not have this, the thing that is passed in
                //is "it" but that is hard for us to understand.
            item ->

            var fragmentToShow: Fragment? =null
            when(item.itemId){
                //Handling action home
                R.id.action_home ->{
                    //navigate to home screen / feed fragment
                    fragmentToShow = FeedFragment()

                }
                //Handling action compose
                R.id.action_compose ->{
                    //navigate to the Compose screen
                    fragmentToShow = ComposeFragment()

                }
                //Handling action profile
                R.id.action_profile ->{
                    fragmentToShow = ProfileFragment()

                }


            }
            //if fragment show is not null then show it
            if(fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            //return true to say that we've handled this user interaction on the item
            true

        }

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
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