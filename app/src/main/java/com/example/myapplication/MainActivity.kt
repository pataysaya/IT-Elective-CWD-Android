package com.example.myapplication

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var btnLogout: Button
    private lateinit var btnAddItem: Button
    private lateinit var scrolllayout: ScrollView
    private val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()

        database = Firebase.database.reference

        val db2 = FirebaseFirestore.getInstance()
        val itemsCollection = db2.collection("Items")

        val linearLayout = findViewById<LinearLayout>(R.id.listLayout)

        itemsCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.data["Name"].toString()
                    val description = document.data["Description"].toString()
                    val photoURL = document.data["photoURL"].toString()
                    val price = String.format("Php %.2f", document.data["Price"].toString().toFloat())

                    val cardView = CardView(this)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(10, 10, 10, 10)
                    cardView.layoutParams = layoutParams
                    cardView.cardElevation = 20f
                    cardView.radius = 20f
                    cardView.setCardBackgroundColor(resources.getColor(R.color.white, null))
                    cardView.setContentPadding(16, 0, 16, 16)

                    val contentLayout = LinearLayout(this)
                    contentLayout.orientation = LinearLayout.VERTICAL
                    cardView.addView(contentLayout)

                    val imageView = ImageView(this)
                    Glide.with(this).load(photoURL).into(imageView)
                    val imageLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT ,
                        resources.getDimensionPixelSize(R.dimen.hundred30dp)
                    )

                    imageLayoutParams.gravity = Gravity.CENTER
                    imageView.layoutParams = imageLayoutParams
                    contentLayout.addView(imageView)

                    val nameTextView = TextView(this)
                    val nameLayout = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    nameTextView.text = "$name"
                    nameTextView.textSize = 20F
                    nameTextView.setTypeface(null, Typeface.BOLD)
                    nameLayout.gravity = Gravity.CENTER
                    nameTextView.gravity = Gravity.CENTER
                    nameTextView.layoutParams = nameLayout
                    contentLayout.addView(nameTextView)
                    nameTextView.setTextColor(getColor(R.color.black))

                    val priceDetails = LinearLayout(this)
                    priceDetails.orientation = LinearLayout.HORIZONTAL
                    priceDetails.gravity = Gravity.CENTER

                    contentLayout.addView(priceDetails)

                    val priceTextView = TextView(this)
                    val priceLayout = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    priceTextView.setTypeface(null, Typeface.BOLD)
                    priceTextView.textSize = 16F
                    priceTextView.text = "$price"
                    priceTextView.layoutParams = priceLayout
                    priceDetails.addView(priceTextView)
                    priceTextView.setTextColor(getColor(R.color.custom_red))


                    val spaceView = View(this)
                    spaceView.layoutParams = LinearLayout.LayoutParams(
                        resources.getDimensionPixelSize(R.dimen.pad_small),
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    priceDetails.addView(spaceView)

                    // Add the CardView to the LinearLayout
                    linearLayout.addView(cardView)

                    cardView.setOnClickListener{
                        val intent = Intent(this, ViewDetailsActivity::class.java).apply {
                            putExtra("itemName", name)
                            putExtra("itemDescription", description)
                            putExtra("itemPrice", price)
                            putExtra("imageUrl", photoURL)
                        }
                        startActivity(intent)

                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("ZZZ",    "Error getting documents.", exception)
            }

        btnLogout = findViewById<Button>(R.id.btnLogout)
        btnAddItem = findViewById<Button>(R.id.btnAddItem)



        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnAddItem.setOnClickListener {
            var intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser === null) {
            Log.d("ZZZZZZ", "No user logged in.")
            val login = Intent (this, LoginActivity::class.java)
            startActivity(login)
        }
    }

}