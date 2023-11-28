package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAddItemBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage


class AddItemActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAddItemBinding

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    @IgnoreExtraProperties
    data class Item(
        val Name: String,
        val Description: String,
        val Price: String,
        val dateCreated: Timestamp,
        val photoURL: String
    )
    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseApp.initializeApp(this)
        //val storageRef = FirebaseStorage.getInstance().reference

        binding = ActivityAddItemBinding.inflate((layoutInflater))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                if (it.data != null){
                    val ref = Firebase.storage.reference.child("images")

                    ref.putFile(it.data!!.data!!).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            binding.imageview4?.setImageURI(it)
                            Toast.makeText(this@AddItemActivity, "Photo Uploaded", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        binding.uploadButton?.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "images/*"
            imageLauncher.launch(intent)
        }



        auth = Firebase.auth

        val etItemName = findViewById<EditText>(R.id.itemNameET)
        val etDescription = findViewById<EditText>(R.id.descET)
        val etPrice = findViewById<EditText>(R.id.priceET)
        val etImageUrlFirst = findViewById<EditText>(R.id.imageURLET)

        val buttonAdd = findViewById<Button>(R.id.addButton)

        buttonAdd.setOnClickListener {
            val val_itemName = etItemName.text.toString()
            val val_description = etDescription.text.toString()
            val val_price = etPrice.text.toString()
            val val_timestamp = Timestamp.now()
            val val_imageUrlFirst = etImageUrlFirst.text.toString()

            if(val_itemName.equals("") || val_description.equals("") || val_price.equals("") || val_imageUrlFirst.equals("")){
                if(val_itemName.equals("")){
                    etItemName.setHintTextColor(R.color.custom_red)
                }
                if(val_description.equals("")){
                    etDescription.setHintTextColor(R.color.custom_red)
                }
                if(val_price.equals("")){
                    etPrice.setHintTextColor(R.color.custom_red)
                }
                if(val_imageUrlFirst.equals("")){
                    etImageUrlFirst.setHintTextColor(R.color.custom_red)
                }

                if(!val_itemName.equals("")){
                    etItemName.setTextColor(R.color.black)
                }
                if(!val_description.equals("")){
                    etDescription.setTextColor(R.color.black)
                }
                if(!val_price.equals("")){
                    etPrice.setTextColor(R.color.black)
                }
                if(!val_imageUrlFirst.equals("")){
                    etImageUrlFirst.setTextColor(R.color.black)
                }

                Toast.makeText(baseContext,"Required fields.",Toast.LENGTH_SHORT).show()
            }else {
                val item = Item(val_itemName, val_description, val_price,val_timestamp, val_imageUrlFirst)
                addItem(item)
            }
        }
        val backbutton2 = findViewById<Button>(R.id.backbutton2)
        backbutton2.setOnClickListener {
            finish()
        }
    }

    fun updateUI(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun addItem(item: Item){
        Log.d("","${item.Name} - ${item.Description} - ${item.Price} - ${item.dateCreated} - ${item.photoURL}")
        db.collection("Items")
            .add(item)
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext,
                    "${item.Name} added.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    baseContext,
                    "${e.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }
}