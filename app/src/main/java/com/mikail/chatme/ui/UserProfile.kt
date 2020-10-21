package com.mikail.chatme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
import kotlinx.android.synthetic.main.activity_user_profile.view.*
import java.lang.IllegalStateException

class UserProfile : DialogFragment(){
    private var userRef = Firebase.firestore.collection("Users")
    lateinit var ud:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ud = arguments?.getString("id")?:throw IllegalStateException("No arguenmts Provided")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView:View = inflater.inflate(R.layout.activity_user_profile,container,false)

        userRef.whereEqualTo("userId",ud)
            .addSnapshotListener { value, error ->

                error.let {

                }

                value?.let {

                    for (document in value.documents)
                    {
                        val user = document.toObject<User>()
                        if (user!=null)
                        {
                           rootView.username.text = user.username
                            rootView.stack.text = user.stack
                            val  userStack= " ${user.email} Developer"
                            rootView.email.text = userStack
                            if (user.userImage == "default")
                            {
                                rootView.userImage.setImageResource(R.drawable.defaultimage)
                            }
                            else{
                                Picasso.get().load(user.userImage).into(rootView.userImage)
                            }


                        }
                    }

                }
            }
        return rootView
    }
companion object{
    private const val userId = ""
    fun newInstance(item:String):UserProfile = UserProfile().apply {
        arguments = Bundle().apply {
            putString("id",userId)
        }
    }
}

}