package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //PhoneNumber Authentication
    private lateinit var fireBaseAuthentication : FirebaseAuth
    private lateinit var enterPhoneNumber : AppCompatEditText
    private lateinit var sentOtpButton : AppCompatButton

    private lateinit var enterVerifyOtp : AppCompatEditText
    private lateinit var verifyOtpButton : AppCompatButton
    private var verificationID = ""

    //Google Authentication
    private lateinit var googleSignInClient : GoogleSignInClient
//    private lateinit var googleButton : AppCompatButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Phone Authentication id Access
        enterPhoneNumber = findViewById(R.id.enter_phone_Number)
        sentOtpButton = findViewById(R.id.send_otp)

        enterVerifyOtp = findViewById(R.id.enter_otp)
        verifyOtpButton = findViewById(R.id.veryFyButton_otp)

        fireBaseAuthentication = FirebaseAuth.getInstance()

        //Send otp
        sentOtpButton.setOnClickListener {
            sendOtp()
        }

        //Verification otp
        verifyOtpButton.setOnClickListener {
            verifyOtp()
        }

        //Google Authentication id Access
        findViewById<SignInButton>(R.id.google_authentication).setOnClickListener {
            googleSignIn()
        }

        // GoogleSignInOption
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

    }

    private fun sendOtp(){
        val phoneAuthentication = PhoneAuthOptions.newBuilder()
            .setPhoneNumber("+91${enterPhoneNumber.text}")
            .setActivity(this)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Toast.makeText(this@MainActivity, "Verification Successful", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    Toast.makeText(this@MainActivity, "Verification Failed ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, p1)
                    verificationID = verificationId
                    Toast.makeText(this@MainActivity, "Otp is sent", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthentication)
    }

    private fun verifyOtp(){
        val verifyOtp = enterVerifyOtp.text.toString()
        val userPhoneCredential = PhoneAuthProvider.getCredential(verificationID, verifyOtp)

        fireBaseAuthentication.signInWithCredential(userPhoneCredential).addOnSuccessListener {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }

    //GoogleSignIn create a Function
    private fun googleSignIn(){
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val userTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            manageResults(userTask)
        }
    }

    private fun manageResults(userTask: Task<GoogleSignInAccount>) {
        val userAccount : GoogleSignInAccount? = userTask.result

        if (userAccount != null){
            val credential = GoogleAuthProvider.getCredential(userAccount.idToken, null)
            fireBaseAuthentication.signInWithCredential(credential).addOnSuccessListener {
                if (userTask.isSuccessful){
                    startActivity(Intent(this, GoogleSignActivity::class.java))
                    Toast.makeText(this, "Google Account Created", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, userTask.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this, userTask.exception?.message, Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this, userTask.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }
}
