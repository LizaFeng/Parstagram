package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Check if there's a user logged in
        //If there is, take them to MainActivity
        if (ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }
        //Setting up an onClickListener for the login button
        findViewById<Button>(R.id.login_button).setOnClickListener{
            val username= findViewById<EditText>(R.id.et_userName).text.toString()
            val password=findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }

        //Setting up an onClickListener for the sign up button
        findViewById<Button>(R.id.signupBtn).setOnClickListener{
            val username= findViewById<EditText>(R.id.et_userName).text.toString()
            val password=findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    //For user sign up
    private fun signUpUser(username: String, password: String){
        //Create the ParseUser
        val user = ParseUser()

        //Sets fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground{e->
            if (e == null){
                //Hooray! let them use the app now
                //TODO: Show a toast to indicate user success
                    Toast.makeText(this,"Successfully signed up",Toast.LENGTH_SHORT).show()
                //TODO: Navigate the user to the MainActivity
                goToMainActivity()


            }else{
                //TODO: Show a Toast to show that login was not successful
                //Sign up didnt succeed. Look at the ParseException
                //to figure out what went wrong.
                e.printStackTrace()
            }

        }
    }

    private fun loginUser(username: String, password:String){
        //The network call is in the background because we dont know how long it will take.
        ParseUser.logInInBackground(username,password, ({ user, e ->
            if (user !=null){
                //Hooray! The user is logged in
                Log.i(TAG, "Successfully logged in user")
                goToMainActivity()
            }else{
                //Signup failed. Look at the ParseException to see what happened
                e.printStackTrace()
                Toast.makeText(this, "Error loggin in", Toast.LENGTH_SHORT).show()

            }
        }))
    }

    //Creating a method to enter main activity
    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        //once the user successfully logged in, we close out the login activity so that the
        //back button would exit the app instead of bringing the user back to login activity
        finish()
    }

    companion object{
        const val TAG="LoginActivity"
    }
}