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
                    Log.d("error",it.toString())
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
                                userImage.setImageResource(R.drawable.person)
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
//        database = FirebaseDatabase.getInstance().getReference("Users").child(receiverId!!)
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val data = snapshot.getValue(User::class.java)
//                if (snapshot.exists()) {
//                    username.text = data?.username
//                    if (data?.userImage == "default") {
//                        userImage.setImageResource(R.drawable.person)
//                    }
//                    else {
//                        Picasso.get().load(data?.userImage).into(userImage)
//                    }
//                }
//
//                data?.userImage?.let { readMessages(fUser.uid,receiverId, it) }
//
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@MessageActivity, "Sorry An error occured", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
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

        dbRef.addSnapshotListener { value, error ->

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
//        var reference = FirebaseDatabase.getInstance().getReference("Chats")
//        reference.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                chatMessage.clear()
//               for (data in snapshot.children)
//               {
//                    val std = data.getValue(MessageModel::class.java)
//                   if (std?.sender.equals(myId)&& std?.receiver.equals(userId) || std?.sender.equals(userId) && std?.receiver.equals(myId))
//                   {
//                       if (std != null) {
//                           chatMessage.add(std)
//                       }
//                   }
//               }
//
//                adapter = MessageAdapter(chatMessage,image)
//                recyclerView.adapter = adapter
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}