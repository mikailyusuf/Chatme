package com.mikail.chatme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikail.chatme.R
import com.mikail.chatme.adapters.ViewpagerAdapter
import com.mikail.chatme.authUi.LoginActivity
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ChatsActivity : AppCompatActivity() {
    lateinit var fUser: FirebaseUser
    lateinit var database: DatabaseReference
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    private var userRef = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        fUser = FirebaseAuth.getInstance().currentUser!!
//        database = FirebaseDatabase.getInstance().getReference("Users").child(fUser.uid)
//        database.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val data = snapshot.getValue(User::class.java)
//                    username.text = data?.username
//                    if (data?.userImage == "default") {
//                        userImage.setImageResource(R.drawable.person)
//                    }
//                    else {
//                        Picasso.get().load(data?.userImage).into(userImage)
//                    }
//
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@ChatsActivity, "Sorry An error occured", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewpager)
        viewPager!!.adapter = ViewpagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        TabLayoutMediator(
            tabLayout!!,
            viewPager!!,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "USERS"
                    1 -> tab.text = "CHATS"
                }
            }).attach()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
                return true

            }

            R.id.profile->{

                startActivity(Intent(this,ProfileActivity::class.java))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun status(status:Boolean) = CoroutineScope(Dispatchers.IO).launch {
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
                catch (e:Exception){
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





}

