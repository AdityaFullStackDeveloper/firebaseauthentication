package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var enterYourRegisterEmail: AppCompatEditText
    private lateinit var enterYourRegisterPassword: AppCompatEditText
    private lateinit var registerButton: AppCompatButton
    private lateinit var userLogin : TextView

    @SuppressLint("CommitPrefEdits", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        enterYourRegisterEmail = findViewById(R.id.enterYour_registerEmail)
        enterYourRegisterPassword = findViewById(R.id.enterYour_registerPassword)
        registerButton = findViewById(R.id.registerButton)
        userLogin = findViewById(R.id.Login_in)

        userLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()

//        // Email Access
//        val user = auth.currentUser
//        val email = user?.email
//        if (email != null){
//
//        }
//
//        // Phone number Access
//        val userPhone = auth.currentUser
//        val phone = userPhone?.phoneNumber
//        if (phone != null){
//
//        }

        registerButton.setOnClickListener {
            val userEmail = enterYourRegisterEmail.text.toString()
            val userPassword = enterYourRegisterPassword.text.toString()

            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Registration Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Empty fields are not allowed !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
