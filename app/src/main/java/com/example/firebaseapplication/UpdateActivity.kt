package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class UpdateActivity : AppCompatActivity() {
    private lateinit var updateNameEditText : AppCompatEditText
    private lateinit var updateEmailEditText : AppCompatEditText
    private lateinit var updateYourData : AppCompatButton
    private var dataBase = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        updateNameEditText = findViewById(R.id.update_name)
        updateEmailEditText = findViewById(R.id.update_Email)
        updateYourData = findViewById(R.id.update_yourData)

        updateData()

        updateYourData.setOnClickListener {
            val updateName = updateNameEditText.text.toString()
            val updateEmail = updateEmailEditText.text.toString()
            val updateUserId = FirebaseAuth.getInstance().currentUser!!.uid

            val updateMap = mapOf(
                "userName" to updateName,
                "userEmail" to updateEmail,
                "id" to updateUserId
            )

            dataBase.collection("user").document(updateUserId).update(updateMap)
            Toast.makeText(this, "Updated Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, FetchDataActivity::class.java))
        }
    }

    private fun updateData(){
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = dataBase.collection("user").document(userUid)
        reference.get().addOnSuccessListener {
            if (it != null){
                val name = it.data?.get("userName").toString()
                val email = it.data?.get("userEmail").toString()


                updateNameEditText.setText(name)
                updateEmailEditText.setText(email)
//                updatePasswordEditText.setText(password)
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()
            }
    }
}
