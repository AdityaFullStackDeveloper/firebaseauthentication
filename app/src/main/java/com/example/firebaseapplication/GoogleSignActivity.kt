package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class GoogleSignActivity : AppCompatActivity() {

    private val collectDataBase = FirebaseFirestore.getInstance()
    private lateinit var enterYourName: AppCompatEditText
    private lateinit var enterYourEmail: AppCompatEditText
    private lateinit var enterYourPassword: AppCompatEditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign)

        enterYourName = findViewById(R.id.enter_yourName)
        enterYourEmail = findViewById(R.id.enter_yourEmail)
        enterYourPassword = findViewById(R.id.enter_yourPassword)

        findViewById<AppCompatButton>(R.id.save_userData).setOnClickListener {
            setData()
        }
    }

    private fun setData() {
        val saveName = enterYourName.text.toString().trim()
        val saveEmail = enterYourEmail.text.toString().trim()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val userDataMap = hashMapOf(
            "userName" to saveName,
            "userEmail" to saveEmail,
            "id" to userId
        )

        collectDataBase.collection("user").document(userId).set(userDataMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Added successful", Toast.LENGTH_SHORT).show()
                enterYourName.text!!.clear()
                enterYourEmail.text!!.clear()
                enterYourPassword.text!!.clear()

                startActivity(Intent(this, FetchDataActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data Added failed", Toast.LENGTH_SHORT).show()
            }
    }
}