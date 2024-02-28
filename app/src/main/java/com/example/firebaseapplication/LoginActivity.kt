package com.example.firebaseapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var enterYourLoginEmail: AppCompatEditText
    private lateinit var enterYourLoginPassword: AppCompatEditText
    private lateinit var loginButton: AppCompatButton

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enterYourLoginEmail = findViewById(R.id.enterYour_loginEmail)
        enterYourLoginPassword = findViewById(R.id.enterYour_loginPassword)
        loginButton = findViewById(R.id.user_login)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val userLoginEmail = enterYourLoginEmail.text.toString()
        val userLoginPassword = enterYourLoginPassword.text.toString()

        if (userLoginEmail.isNotEmpty() && userLoginPassword.isNotEmpty()) {
            auth.signInWithEmailAndPassword(userLoginEmail, userLoginPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, GoogleSignActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Login failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
        }
    }
}
