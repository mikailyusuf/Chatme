package com.mikail.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener {
                val it = Intent(this,LoginActivity::class.java)
            startActivity(it)
        }

        signup.setOnClickListener {
            val it = Intent(this,RegistrationActivity::class.java)
            startActivity(it)
        }
    }
}