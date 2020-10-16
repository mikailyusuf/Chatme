package com.mikail.chatme.authUi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mikail.chatme.Internet
import com.mikail.chatme.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        resetBtn.setOnClickListener {

            if (email.text.isNullOrEmpty()) {
                email.setError("Email field is required")


            } else {
                val email = email.text.toString()
                if (Internet.isNetworkConnected(this)) {
                    mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "A password Reset Link has been sent to the Email Provided ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else
                            {
                                Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                            }

                        }

                }
            }

        }
    }
}

