package com.mikail.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
//     lateinit var fUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val fUser = FirebaseAuth.getInstance().currentUser
        if (fUser != null) {
            val it = Intent(this, ChatsActivity::class.java)
            startActivity(it)
            finish()
        }

        login.setOnClickListener {
            val it = Intent(this, LoginActivity::class.java)
            startActivity(it)
        }

        signup.setOnClickListener {
            val it = Intent(this, RegistrationActivity::class.java)
            startActivity(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val fUser = FirebaseAuth.getInstance().currentUser

        if (fUser != null) {
            val it = Intent(this, ChatsActivity::class.java)
            startActivity(it)
            finish()
        }

    }
}