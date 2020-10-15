package com.mikail.chatme.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.ui.ChatsActivity
import com.mikail.chatme.R

import com.mikail.chatme.models.User
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private var userRef = Firebase.firestore.collection("Users")
    private var mAuth: FirebaseAuth? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Users")
    lateinit var mStack: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()


        val stack = resources.getStringArray(R.array.stacks)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stack)
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    mStack = parent.getItemAtPosition(position).toString()
                    if (mStack == "none")
                    {
                        noStack.visibility = View.VISIBLE
                        mStack = noStack.text.toString()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }


        signup.setOnClickListener {

            verifyInputs()

            val mUseranme = username.text?.trim().toString()
            val mEmail = email.text?.trim().toString()
            val mPassword = password?.text?.trim().toString()

            register(mEmail, mPassword, mUseranme, mStack)

        }


    }

    fun verifyInputs() {
        if (username.text.isNullOrEmpty()) {
            username.error = "Username is Required"
            username.requestFocus()

        }
        if (email.text.isNullOrEmpty()) {
            email.error = "Email is Required"
            email.requestFocus()
        }

        if (password.text.isNullOrEmpty()) {
            password.error = "Password is Required"
            password.requestFocus()
        }

    }


    private  fun registerUserToDb(user: User) = CoroutineScope(Dispatchers.IO).launch {

        try {
            userRef.add(user).await()

        }

        catch (e:Exception){

        }
    }

    private fun register( email: String, password: String, username: String, stack: String) {

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {

            if (it.isSuccessful) {
                val userId = mAuth?.currentUser?.uid
                val user = userId?.let { it1 ->
                    User(
                        it1,
                        username,
                        email,
                        stack,
                        userImage = "default"
                    )
                }
                if (userId != null) {
                    if (user != null) {
                        registerUserToDb(user)
                    }

                }

            } else {
                Toast.makeText(
                    this,
                    "Cant register User Please Check your email or Password.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}