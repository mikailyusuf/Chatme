package com.mikail.chatme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message.userImage
import kotlinx.android.synthetic.main.activity_message.username
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfile : AppCompatActivity() {
    private var userRef = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val id = intent.getStringExtra("id")


        userRef.whereEqualTo("userId",id)
            .addSnapshotListener { value, error ->

                error.let {

                }

                value?.let {

                    for (document in value.documents)
                    {
                        val user = document.toObject<User>()
                        if (user!=null)
                        {
                            username.text = user.username
                            stack.text = user.stack
                            val  userStack= " ${user.email} Developer"
                            email.text = userStack
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
}