package com.mikail.chatme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.adapters.MessageAdapter
import com.mikail.chatme.models.MessageModel
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.activity_chats.userImage
//import kotlinx.android.synthetic.main.activity_chats.username
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MessageActivity : AppCompatActivity() {
    lateinit var fUser: FirebaseUser
    lateinit var database: DatabaseReference
    lateinit var adapter:MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private  lateinit var chatMessage: MutableList<MessageModel>
    private var dbRef = Firebase.firestore.collection("Chats")
    private var userRef = Firebase.firestore.collection("Users")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        var linearLayout = LinearLayoutManager(this)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager =linearLayout



        val receiverId = intent.getStringExtra("userId")
        fUser = FirebaseAuth.getInstance().currentUser!!
        val id = fUser.uid

        userRef.whereEqualTo("userId",receiverId)
            .addSnapshotListener { value, error ->

                error.let {

                }

                value?.let {
                    for (documents in value.documents)
                    {
                        val user = documents.toObject<User>()
                        if (user!=null)
                        {
                            username.text = user.username
                            if (user.userImage == "default")
                            {
                                userImage.setImageResource(R.drawable.defaultimage)
                            }
                            else{
                                Picasso.get().load(user.userImage).into(userImage)
                            }

                            if (receiverId != null) {
                                readMessages(fUser.uid,receiverId,"")
                            }
                        }
                    }
                }
            }

        send.setOnClickListener {
           val time =  System.currentTimeMillis()

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

    fun sendMessage(sender:String,receiver:String,message:String) = CoroutineScope(Dispatchers.IO).launch {

        val messageModel = MessageModel(sender, receiver, message)
        dbRef.add(messageModel).await()

    }
//    {

//        var reference:DatabaseReference = FirebaseDatabase.getInstance().reference
//        val key = reference.child("Chats").push().key
//        if (key != null) {
//            reference.child("Chats").child(key).setValue(messageModel)
//        }
//    }


    fun readMessages(senderId:String,receiverId:String,image:String)
    {
        chatMessage = mutableListOf(MessageModel())

        dbRef
            .addSnapshotListener { value, error ->

                error.let {
                }

                value?.let {
                    chatMessage.clear()
                    for (documents in value.documents)
                    {
                        val messages = documents.toObject<MessageModel>()
                        if (messages!=null)
                        {
                            if (messages.sender == senderId && messages.receiver == receiverId  || messages.receiver == senderId && messages.sender == receiverId  )
                            {
                                chatMessage.add(messages)
                                adapter = MessageAdapter(chatMessage)
                                recyclerView.adapter = adapter
                            }

                        }
                    }
                }

            }

    }

    private fun status(status:Boolean) = CoroutineScope(Dispatchers.IO).launch {
        val map = mutableMapOf<String, Boolean>()
        map["status"] = status
        val userQuery = userRef.whereEqualTo("userId",FirebaseAuth.getInstance().currentUser?.uid).get().await()
        if (userQuery.documents.isNotEmpty())
        {
            for (document in userQuery)
            {
                try {
                    userRef.document(document.id).set(status, SetOptions.merge()).await()

                }
                catch (e: Exception){
                    Log.d("userStatus",e.toString())
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        status(true)
    }

    override fun onPause() {
        super.onPause()
        status(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}