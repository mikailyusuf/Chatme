package com.mikail.chatme.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.mikail.chatme.R
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.email
import kotlinx.android.synthetic.main.activity_profile.userImage
import kotlinx.android.synthetic.main.activity_profile.username
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


private const val REQUEST_CODE_IMAGE_PICK = 100

class ProfileActivity : AppCompatActivity() {
    private var profileImageUrl = ""
    private val imageRef = Firebase.storage.reference
    private var imageUri: Uri? = null
    private var userRef = Firebase.firestore.collection("Users")
    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

       id = FirebaseAuth.getInstance().currentUser?.uid.toString()

        userImage.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(
                    it, REQUEST_CODE_IMAGE_PICK
                )
            }
        }

        userRef.whereEqualTo("userId", id)
            .addSnapshotListener { value, error ->

                error.let {

                }

                value?.let {
                    for (document in value.documents) {
                        val user = document.toObject<User>()

                        if (user != null) {
                            username.text = user.username
                            email.text = user.email
                            if (user.userImage == "default")
                            {
                                userImage.setImageResource(R.drawable.defaultimage)
                            }
                            else{
                                Picasso.get().load(user.userImage).into(userImage)
                            }

                        }


                    }


                }
            }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val ref = imageRef.child("profileImages/" + UUID.randomUUID().toString())
            val uploadTask = ref.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {

                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                imageUri = it
                userImage.setImageURI(it)
                uploadImage()
            }
        }
    }

    private fun updateProfileImage(image:String) = CoroutineScope(Dispatchers.IO).launch {
        val map = mutableMapOf<String, String>()
        map["userImage"] = image
        val userQuery = userRef.whereEqualTo("userId",id).get().await()
        if (userQuery.documents.isNotEmpty())
        {
            for (document in userQuery)
            {
                try {
                    userRef.document(document.id).set(map, SetOptions.merge()).await()

                }
                catch (e: Exception){
                    Log.d("userStatus",e.toString())
                }

            }
        }

    }



}