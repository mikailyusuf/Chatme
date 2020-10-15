package com.mikail.chatme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mikail.chatme.R
import com.mikail.chatme.authUi.LoginActivity
import com.mikail.chatme.authUi.SignUpActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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
            val it = Intent(this, SignUpActivity::class.java)
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