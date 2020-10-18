package com.mikail.chatme.authUi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.mikail.chatme.Internet
import com.mikail.chatme.ui.ChatsActivity
import com.mikail.chatme.R

import com.mikail.chatme.models.User
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

private const val REQUEST_CODE_IMAGE_PICK = 100

class SignUpActivity : AppCompatActivity() {
    private var userRef = Firebase.firestore.collection("Users")
    private var mAuth: FirebaseAuth? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Users")
    lateinit var mStack: String
    private var profileImageUrl = ""
    private val imageRef = Firebase.storage.reference
    private var imageUri: Uri? = null


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
                    if (mStack == "Other")
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

        profile_image.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(
                    it,
                    REQUEST_CODE_IMAGE_PICK
                )
            }

        }


        signup.setOnClickListener {
            if (Internet.isNetworkConnected(this))
            {
                if (username.text.isNullOrEmpty()) {
                    username.error = "Username is Required"
                    username.requestFocus()
                    return@setOnClickListener
                }
                if (email.text.isNullOrEmpty()) {
                    email.error = "Email is Required"
                    email.requestFocus()
                    return@setOnClickListener
                }
                if (mStack.isNullOrEmpty())
                {
                    spinner.requestFocus()
                    return@setOnClickListener
                }
                if (password.text.isNullOrEmpty()) {
                    password.error = "Password is Required"
                    password.requestFocus()
                    return@setOnClickListener
                }

                progressBar.visibility = View.VISIBLE
                if (mStack == "Other")
                {
                    mStack = noStack.text.toString()
                }

                val mUseranme = username.text?.trim().toString()
                val mEmail = email.text?.trim().toString()
                val mPassword = password?.text?.trim().toString()

                register(mEmail, mPassword, mUseranme, mStack,profileImageUrl)
            }
            else{
                Toast.makeText(this,"Sorry You ar not Connected to the Internet",Toast.LENGTH_SHORT).show()
            }




        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                imageUri = it
                profile_image.setImageURI(it)
                progressBar.visibility = View.VISIBLE
                uploadImage()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            progressBar.visibility = View.VISIBLE
            signup.visibility = View.INVISIBLE
            val ref = imageRef.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE
                    signup.visibility = View.VISIBLE


                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE
                    signup.visibility = View.VISIBLE


                    profileImageUrl = task.result.toString()
                } else {
                    // Handle failures
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Sorry an Error Occured", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()

        }
    }


    private  fun registerUserToDb(user: User) = CoroutineScope(Dispatchers.IO).launch {

        try {
            userRef.add(user).await()
            withContext(Dispatchers.Main)
            {
                progressBar.visibility = View.INVISIBLE
                startActivity(Intent(this@SignUpActivity,ChatsActivity::class.java))
            }
        }
        catch (e:Exception){
            withContext(Dispatchers.Main)
            {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun register(email: String, password: String, username: String, stack: String, profileImage:String) {

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {

            if (it.isSuccessful) {
                progressBar.visibility = View.INVISIBLE

                val userId = mAuth?.currentUser?.uid
                val user = userId?.let { it1 ->
                    User(
                        it1,
                        username,
                        email,
                        stack,
                        profileImage
                    )
                }
                if (userId != null) {
                    if (user != null) {
                        registerUserToDb(user)
                    }

                }

            } else {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(
                    this,
                    "Cant register User Please Check your email or Password.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}