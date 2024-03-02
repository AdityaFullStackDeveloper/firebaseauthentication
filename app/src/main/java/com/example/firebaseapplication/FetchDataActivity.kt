package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FetchDataActivity : AppCompatActivity() {

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var editUserData : ImageView

    private var fetchDataBase = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetchdata)

        userName = findViewById(R.id.userName_textView)
        userEmail = findViewById(R.id.userEmail_textView)
        editUserData = findViewById(R.id.editUserData)

        editUserData.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java))
        }

        val reference = fetchDataBase.collection("user").document(userId)

        reference.get().addOnSuccessListener {
            if (it != null) {
                val name = it.data?.get("userName")?.toString()
                val email = it.data?.get("userEmail")?.toString()

                userName.text = name
                userEmail.text = email
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }
}

