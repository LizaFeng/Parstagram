package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView

    //For step 4: setting an adapter on RecyclerView
    lateinit var adapter: PostAdapter
    //For step 4
    var allPosts: MutableList<Post> = mutableListOf()

    //Creating variable for swipe to refresh layout
    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This is where we set up our views and click listeners

        //we dont need to define what type of view we are looking for because we already defined
        //the postsRecyclerView's type.
        postsRecyclerView= view.findViewById(R.id.postRecyclerView)

        //for swipe to refresh
        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshing", Toast.LENGTH_SHORT).show()
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        //Steps to populate RecyclerView
        //1. Create layout for each row in list (item_post.xml created)
        //2. Create data source for each row (this is the Post class)
        //3. Create adapter that will bridge data and row layout (Post Adapter)
        //4. Set adapter on RecyclerView
        adapter = PostAdapter (requireContext(), allPosts)
        //set adapter for recycler view
        postsRecyclerView.adapter= adapter
        //5. Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }
    //Query for all post in our server (not using get since that gets one object)
    open fun queryPosts(){
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Telling parse to find all the post objects and return it to us
        query.include(Post.KEY_USER)
        //So that we have the newer posts
        query.addDescendingOrder("createdAt")

        //Returning only recent 20 post
        query.setLimit(20)
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                //If the exception is not null then...
                if ( e !=  null){
                    //Something went wrong
                    Log.e(MainActivity.TAG, "Error fetching posts")
                }else{
                    if(posts != null){
                        //Clear out current list of things
                        adapter.clear()
                        for ( post in posts){
                            Log.i(TAG, "Post: "+ post.getDescription() + " , username: " +
                                    post.getUser()?.username)
                        }
                        //Added after setting up the adapter
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()

                        //For Swipe to refresh: stopping refreshing icon
                        swipeContainer.setRefreshing(false)

                    }
                }
            }

        })
    }

    companion object{
        const val TAG="FeedFragment"
    }
}