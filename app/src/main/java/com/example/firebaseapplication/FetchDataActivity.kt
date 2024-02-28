package com.example.firebaseapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class FetchDataActivity : AppCompatActivity() {
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userPassword : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetchdata)

        userName = findViewById(R.id.userName_textView)
        userEmail = findViewById(R.id.userEmail_textView)
        userPassword = findViewById(R.id.userPassword_textView)


    }
}
