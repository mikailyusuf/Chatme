package com.mikail.chatme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mikail.chatme.R
import com.mikail.chatme.adapters.ViewpagerAdapter
import com.mikail.chatme.authUi.LoginActivity
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chats.*

class ChatsActivity : AppCompatActivity() {
    lateinit var fUser: FirebaseUser
    lateinit var database: DatabaseReference
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
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
                val it = Intent(this, LoginActivity::class.java)
                startActivity(it)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

