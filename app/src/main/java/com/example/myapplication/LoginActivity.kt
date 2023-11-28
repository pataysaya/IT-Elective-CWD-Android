package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val animatedImageView: ImageView = findViewById<ImageView>(R.id.imageView2)
        animatedImageView.setImageResource(R.drawable.cwgif_)

        Glide.with(this).asGif().load(R.drawable.cwgif_).into(animatedImageView)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.email)
        val etPass = findViewById<EditText>(R.id.password)

        val btnLogin = findViewById<Button>(R.id.logButton)
        val btnRegister = findViewById<Button>(R.id.regButton)



        btnLogin.setOnClickListener {
            val val_email = etEmail.text.toString()
            val val_password = etPass.text.toString()

            if(val_email.equals("") || val_password.equals("")){
                if(val_email.equals("")){
                    etEmail.setHintTextColor(R.color.custom_red)
                }
                if(val_password.equals("")) {
                    etPass.setHintTextColor(R.color.custom_red)
                }
                if(!val_email.equals("")){
                    etEmail.setHintTextColor(R.color.black)
                }
                if(!val_password.equals("")){
                    etEmail.setHintTextColor(R.color.black)
                }

                Toast.makeText(this, "Required Fields.", Toast.LENGTH_LONG).show()
            }else {
                signInEmailPass(val_email, val_password)
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun signInEmailPass(email:String, passw:String){
        auth.signInWithEmailAndPassword(email.trim(), passw.trim())
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful) {
                    Log.d("ZZZlog", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                }else {
                    Log.d("ZZZlog", "signInWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }

    fun updateUI(user: FirebaseUser?){

        val etEmail = findViewById<EditText>(R.id.email)
        val etPass = findViewById<EditText>(R.id.password)

        val hintColor: Int = resources.getColor(R.color.custom_red, null)

        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            etEmail.setTextColor(hintColor)
            etPass.setTextColor(hintColor)
            Toast.makeText(
                baseContext,
                "Authentication Failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}