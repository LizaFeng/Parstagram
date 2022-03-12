package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


//Every post object has Description (string), Image (file), and User (User)
//Will refer to the Post class we created in back4app (name has to match exactly)
@ParseClassName("Post")
class Post : ParseObject() {

    //Getter functions for the parts of the Post object
    fun getDescription():String?{
        return getString(KEY_DESCRIPTION)
    }
    fun getImage(): ParseFile?{
        return getParseFile(KEY_IMAGE)
    }
    fun getUser(): ParseUser?{
        return getParseUser(KEY_USER)
    }

    //Setter functions for the parts of the Post object
    fun setDescription(description: String){
        //put() special Parse method that allows putting of specific key with a specific value
        put(KEY_DESCRIPTION, description)
    }
    fun setImage(parsefile: ParseFile){
        put(KEY_IMAGE, parsefile)
    }
    fun setUser(user: ParseUser){
        put(KEY_USER, user)
    }

    //keys in this case: name of each column in back4app
    companion object{
        const val KEY_DESCRIPTION= "description"
        const val KEY_IMAGE= "image"
        const val KEY_USER= "user"
    }

}