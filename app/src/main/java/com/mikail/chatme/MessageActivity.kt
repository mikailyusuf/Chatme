package com.mikail.chatme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chats.userImage
import kotlinx.android.synthetic.main.activity_chats.username
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {
    lateinit var fUser: FirebaseUser
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val receiverId = intent.getStringExtra("userId")
        fUser = FirebaseAuth.getInstance().currentUser!!
        val id = fUser.uid
        database = FirebaseDatabase.getInstance().getReference("Users").child("userId")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue(UserModel::class.java)
                    username.text = data?.username
                    if (data?.userImage == "default") {
                        userImage.setImageResource(R.drawable.person)
                    }
                    else {
                        Picasso.get().load(data?.userImage).into(userImage)
                    }

                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity, "Sorry An error occured", Toast.LENGTH_SHORT)
                    .show()
            }
        })


        send.setOnClickListener {

            val message = emesssage.text.toString()
            if (message.isNullOrEmpty())
            {
                    Toast.makeText(this,"You cant send an empty message",Toast.LENGTH_SHORT).show()
            }

            else{
                if (receiverId != null) {
                    sendMessage(id,receiverId,message)
                    emesssage.text.clear()
                }
            }

        }

    }

    fun sendMessage(sender:String,receiver:String,message:String)
    {
        val messageModel = MessageModel(sender,receiver,message)
        var reference:DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.child("Chats").setValue(messageModel)


    }
}