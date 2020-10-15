package com.mikail.chatme.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.adapters.OnUserClick
import com.mikail.chatme.adapters.UsersAdapter
import com.mikail.chatme.models.User


class UsersFragment : Fragment(), OnUserClick {
    private lateinit var recyclerView: RecyclerView
    private  lateinit var usersList: MutableList<User>
    private lateinit var usersDatabase: DatabaseReference
    private var dbRef = Firebase.firestore.collection("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
            setHasFixedSize(true)
        }

        usersList = mutableListOf()

        getUsers()
    }
    fun getUsers()
    {
        dbRef.addSnapshotListener{ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            error?.let {
//                progressBar.visibility = View.INVISIBLE
                Toast.makeText(activity,"Sorry cant get Users at this time",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            value?.let {
                for(documents in value.documents)
                {
                    val users = documents.toObject<User>()
                    if (users != null) {
                        if (!users.userId.equals( FirebaseAuth.getInstance().currentUser!!)) {
                            usersList.add(users)
                        }
                    }

//                        progressBar.visibility = View.INVISIBLE
                    val adapter = UsersAdapter(
                        usersList,this)
                    recyclerView.adapter = adapter
                    // progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()

                }

            }
        }
    }


    override fun onUserClick(datamodel: User, position: Int) {
        val intent = Intent(activity, MessageActivity::class.java)
        intent.putExtra("userId",datamodel.userId)
        startActivity(intent)

    }
}