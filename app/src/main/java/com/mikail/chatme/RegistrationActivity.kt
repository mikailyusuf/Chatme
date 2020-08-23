package com.mikail.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    //    private var init = FirebaseApp.initializeApp(this)
//    FirebaseApp.initializeApp(this)
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

    private fun register(email: String, password: String, username: String, stack: String) {
        val user = UserModel(username, email, stack, userImage = "default")
//        val myRef = database?.getReference("Users")
        Log.d("mytest", user.toString())
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {

            if (it.isSuccessful) {
                val userId = mAuth?.currentUser?.uid
                if (userId != null) {
                    myRef.child(userId).setValue(user)
                }
                val intent = Intent(this, ChatsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)


            } else {
               Log.d("mytest", it.exception.toString())

//                Snackbar.make(this,"SORRY",Snackbar.LENGTH_LONG).show()
                Toast.makeText(
                    this,
                    "Cant register User Please Check your email or Password.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}