package com.example.parstagram

import android.app.Application

import com.parse.Parse;
import com.parse.ParseObject

//Our application class

class ParstagramApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //Register your parse models. Registering class we created with parse object
        //Sets us up for using the Post class
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}