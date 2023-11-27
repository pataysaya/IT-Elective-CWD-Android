package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    @IgnoreExtraProperties
    data class User(val displayName: String, val email: String? = null)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val pass2 = findViewById<EditText>(R.id.password2)

        val registerLoginBtn = findViewById<Button>(R.id.registerLoginBtn)

        registerLoginBtn.setOnClickListener {
            val val_email = etEmail.text.toString()
            val val_password = etPassword.text.toString()
            val password2 = pass2.text.toString()

            if(val_email.equals("") || val_password.equals("") || password2.equals("")){
                Toast.makeText(
                    baseContext,
                    "Required fields.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else if(!val_password.equals(password2)){

                Toast.makeText(
                    baseContext,
                    "Password does not match.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            else{
                registerUser(val_email, val_password)
            }
        }

        val backbutton3 = findViewById<Button>(R.id.backbutton3)
        backbutton3.setOnClickListener {
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            Toast.makeText(
                baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun registerUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ZZZlog", "createUserWithEmail: Success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ZZZlog", "createUserWithEmail: Failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }
}