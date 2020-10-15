package com.mikail.chatme.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.adapters.OnUserClick
import com.mikail.chatme.adapters.UsersAdapter
import com.mikail.chatme.models.MessageModel
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chats.*

class ChatFragment : Fragment(), OnUserClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usersList: MutableList<User>
    lateinit var currentUser:String
    private var dbRef = Firebase.firestore.collection("Chats")
    private var userRef = Firebase.firestore.collection("Users")

    //    private lateinit var usersDatabase: DatabaseReference
//    lateinit var chatList:List<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recyclerview)
        usersList = mutableListOf()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)

        }

         currentUser = FirebaseAuth.getInstance().currentUser!!.uid

//        usersDatabase = FirebaseDatabase.getInstance().getReference("Chats")
//        chatList = mutableListOf()


        dbRef.whereEqualTo("sender",currentUser)
            .whereEqualTo("receiver",currentUser)
            .addSnapshotListener { value, error ->

                error.let {
                    Log.d("error",it.toString())
                }

                value?.let {
                    for (documents in value.documents)
                    {
                        val message = documents.toObject<MessageModel>()
                        if (message!=null)
                        {
                            userRef.whereEqualTo("userId",message.receiver)
                                .whereEqualTo("userId",message.sender)
                                .addSnapshotListener { value, error ->

                                    error?.let {
                                        Toast.makeText(activity,"Sorry cant get Users at this time",Toast.LENGTH_SHORT).show()
                                        return@addSnapshotListener
                                    }

                                    value?.let {
                                        for(userl in value.documents)
                                        {
                                            val users = userl.toObject<User>()
                                            if (users != null) {
                                                if (!users.userId.equals( FirebaseAuth.getInstance().currentUser!!)) {
                                                    usersList.add(users)
                                                }
                                            }

                                            val adapter = UsersAdapter(
                                                usersList,this)
                                            recyclerView.adapter = adapter
                                            // progressBar.visibility = View.GONE
                                            adapter.notifyDataSetChanged()

                                        }

                                    }
                                }

                        }
                    }
                }
            }

//        usersDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(
//                    activity,
//                    "Error Encounter Due to " + databaseError.message,
//                    Toast.LENGTH_LONG
//                ).show()/**/
//
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    //before fetch we have clear the list not to show duplicate value
//                    (chatList as MutableList<String>).clear()
//                    // fetch data & add to list
//                    for (data in dataSnapshot.children) {
//                        val std = data.getValue(MessageModel::class.java)
//                        if (std?.sender?.equals(currentUser)!!)
//                        {
//                            (chatList as MutableList<String>).add(std.sender)
//                        }
//                        if (std.receiver.equals(currentUser))
//                        {
//                            (chatList as MutableList<String>).add(std.receiver)
//                        }
//
//                    }
//
//                    readChats()
//
//                } else {
//
//                }
//            }
//        })
    }


    override fun onUserClick(datamodel: User, position: Int) {
        var intent = Intent(activity, MessageActivity::class.java)
        intent.putExtra("userId", datamodel.userId)
        startActivity(intent)
    }


//    fun readChats() {
//        usersDatabase = FirebaseDatabase.getInstance().getReference("Users")
//        usersDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                usersList.clear()
//                for (data in snapshot.children) {
//                    var users = data.getValue(User::class.java)
//                    for (id in chatList) {
//                        if (users?.userId.equals(id)) {
//                            if (usersList.size != 0) {
//                                for (user1 in usersList) {
//                                    if (users?.userId.equals(user1.userId)) {
//                                        if (users != null) {
//                                            usersList.add(users)
//                                            Log.d("devmk", usersList.toString())
//
//                                        }
//                                    }
//                                }
//
//                            } else {
//                                if (users != null) {
//                                    usersList.add(users)
//                                }
//                            }
//                        }
//                    }
//                }
//                val adapter = UsersAdapter(usersList, this@ChatFragment)
//                recyclerView.adapter = adapter
//                // progressBar.visibility = View.GONE
//                adapter.notifyDataSetChanged()
//            }
//
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }


}