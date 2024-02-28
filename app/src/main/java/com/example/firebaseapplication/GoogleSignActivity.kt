package com.example.firebaseapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class GoogleSignActivity : AppCompatActivity() {
    private val dataBase = Firebase.firestore
    private lateinit var enterYourName: AppCompatEditText
    private lateinit var enterYourEmail: AppCompatEditText
    private lateinit var enterYourPassword: AppCompatEditText
//    private lateinit var saveUserData : AppCompatButton
    private lateinit var progressBar: ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign)

        enterYourName = findViewById(R.id.enter_yourName)
        enterYourEmail = findViewById(R.id.enter_yourEmail)
        enterYourPassword = findViewById(R.id.enter_yourPassword)
        progressBar = findViewById(R.id.save_progressBar)

        findViewById<AppCompatButton>(R.id.save_userData).setOnClickListener { 
            progressBar.visibility = View.VISIBLE
            val saveName = enterYourName.text.toString().trim()
            val saveEmail = enterYourEmail.text.toString().trim()
            val savePassword = enterYourPassword.text.toString().trim()
            
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            
            val userDataMap = hashMapOf(
                "userName" to saveName,
                "userEmail" to saveEmail,
                "userPassword" to savePassword
            )
            
            dataBase.collection("user").document(userId).set(userDataMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Added successful", Toast.LENGTH_SHORT).show()
                    enterYourName.text!!.clear()
                    enterYourEmail.text!!.clear()
                    enterYourPassword.text!!.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Data Added failed", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }
}
