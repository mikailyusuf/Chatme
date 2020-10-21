package com.mikail.chatme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.view.*

class UserProfile : DialogFragment() {
    private var userRef = Firebase.firestore.collection("Users")
    lateinit var ud: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ud = arguments?.getString("id") ?: throw IllegalStateException("No arguenmts Provided")

    }

    private fun showProfile(view: View) {
        val id = arguments?.getString("id")
        userRef.whereEqualTo("userId", id)
            .addSnapshotListener { value, error ->

                error.let {

                }

                value?.let {

                    for (document in value.documents) {
                        val user = document.toObject<User>()
                        if (user != null) {
                            view.username.text = user.username
                            val userStack = " ${user.stack} Developer"
                            view.stack.text = userStack
                                view.email.text = user.email
                            if (user.userImage == "default") {
                                view.userImage.setImageResource(R.drawable.defaultimage)
                            } else {
                                Picasso.get().load(user.userImage).into(view.userImage)
                            }


                        }
                    }

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProfile(view)
        view.cancel_button.setOnClickListener {
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_user_profile, container, false)
    }

    companion object {
        private const val USER_ID = "id"
        fun newInstance(id: String): UserProfile = UserProfile().apply {
            arguments = Bundle().apply {
                putString(USER_ID, id)
            }
        }
    }

}