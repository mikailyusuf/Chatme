package com.mikail.chatme.authUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mikail.chatme.Internet
import com.mikail.chatme.ui.ChatsActivity
import com.mikail.chatme.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.email
import kotlinx.android.synthetic.main.activity_registration.password
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    private  var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        loginbtn.setOnClickListener {
            if (Internet.isNetworkConnected(this))
            {

                if(email.text.isNullOrEmpty())
                {
                    email.error = "Email is Required"
                    email.requestFocus()
                    return@setOnClickListener
                }
                if(password.text.isNullOrEmpty())
                {
                    password.error = "Password is Required"
                    password.requestFocus()
                    return@setOnClickListener
                }
                val mEmail = email.text?.trim().toString()
                val mPassword = password?.text?.trim().toString()
                login(mEmail,mPassword)
            }
            else{
                Toast.makeText(this,"Sorry You ar not Connected to the Internet",Toast.LENGTH_SHORT).show()
            }
        }

        signup.setOnClickListener {

            startActivity(Intent(this,SignUpActivity::class.java))
        }

        forgotpassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))

        }
    }

    private fun login(email:String, password:String)
    {
        progressBar.visibility = View.VISIBLE
        mAuth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener(this){
            if (it.isSuccessful)
            {
                progressBar.visibility = View.INVISIBLE
                val intent = Intent(this, ChatsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            else{
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@LoginActivity, "Authentication failed PLease check your Email or Password",
                    Toast.LENGTH_LONG).show() }

        }

    }
}
