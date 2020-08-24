package com.mikail.chatme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ChatFragment : Fragment() {

//    private lateinit var recyclerView: RecyclerView
//    private  lateinit var usersList: MutableList<UserModel>
//    private lateinit var usersDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        recyclerView = view.findViewById(R.id.recyclerview)
//
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
//
//        }
//
//        recyclerView.setHasFixedSize(true)
//        usersDatabase = FirebaseDatabase.getInstance().getReference("Events")
//        usersList = mutableListOf()
//
//        usersDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//               Toast.makeText(activity, "Error Encounter Due to " + databaseError.message, Toast.LENGTH_LONG).show()/**/
//
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    //before fetch we have clear the list not to show duplicate value
//                    usersList.clear()
//                    // fetch data & add to list
//                    for (data in dataSnapshot.children) {
//                        val std = data.getValue(UserModel::class.java)
//                        usersList.add(std!!)
//                    }
//
//                    // bind data to adapter
//                    val adapter = UsersAdapter(usersList, this@ChatFragment)
//                    recyclerView.adapter = adapter
//                    // progressBar.visibility = View.GONE
//                    adapter.notifyDataSetChanged()
//
//                } else {
//
//                }
//            }
//        })
//    }
//
//
//    override fun onUserClick(datamodel: UserModel, position: Int) {
//         Toast.makeText(activity,datamodel.username,Toast.LENGTH_LONG).show()
//    }
}