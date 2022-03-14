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

    //For camera
    val APP_TAG = "MyCustomApp"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1. Setting the description of the pose

        //2. A button to launch the camera to take a picture

        //3. An ImageView to show the picture the user has taken

        //4. A button to save and send the post to our Parse server


        //Setting onCLickListeners for the submit and the take picture button
        findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            //Send post to server
            //1. Grab Description that user inputted
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user= ParseUser.getCurrentUser()
            //Do a null check since photoFile can be null
            if (photoFile != null){
                //Double Exclamation mark is to guarantee photoFile is not null
                submitPost(description, user, photoFile!!)
            }else{
                //TODO print error log message
                //TODO show a toast to the user to let them know to take a picture

            }

        }
        findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            //Launch camera to let user take picture
            onLaunchCamera()
        }

        queryPosts()
    }

    //Send a Post Object to Parse Server
    fun submitPost(description: String, user: ParseUser, file: File) {
        //Create Post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        //Since this is a in the background call, we want to be prepared for exceptions
        post.saveInBackground{ exception ->
            if(exception != null){
                //Something has went wrong
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                //TODO: show a toast to tell user something is wrong with saving post
            }else{
                Log.i(TAG, "Successfully saved post")
                //TODO: Resetting the EditText field to be empty
                //TODO: Reset the ImageView to empty
            }
        }
    }

    //This is for when the user comes back from the camera app and the correct request code is received
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                    //turn file into a bitmap and then put into imageView
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //For launching the camera app. Using an intent to launch the camera app
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        // What are all the apps that can take a picture on the device? User chooses one
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo, save file, then come back to the activity with
                    //the specific request code
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
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