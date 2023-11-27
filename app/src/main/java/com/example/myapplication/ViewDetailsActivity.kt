package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ViewDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)

        val itemName = intent.getStringExtra("itemName")
        val itemDescription = intent.getStringExtra("itemDescription")
        val itemPrice = intent.getStringExtra("itemPrice")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Display the details in TextViews and ImageView
        val itemNameTextView: TextView = findViewById(R.id.itemname)
        val itemDescriptionTextView: WebView = findViewById(R.id.itemdescription)
        val itemPriceTextView: TextView = findViewById(R.id.itemprice)
        val itemImageView: ImageView = findViewById(R.id.itemimage)
        val btnBack: Button = findViewById(R.id.backbutton1)

        itemNameTextView.text = itemName
        itemPriceTextView.text = itemPrice

        Glide.with(this)
            .load(imageUrl)
            .into(itemImageView)

        var counter = 0
        itemPriceTextView.setOnClickListener {
            if(counter == 0) {
                itemPriceTextView.textSize = 24f
                counter += 1
            }
            else{
                counter -= 1
                itemPriceTextView.textSize = 18f
            }
        }
        var counter2 = 0
        itemImageView.setOnClickListener {
            if(counter2 == 0) {
                val newScale = itemImageView.scaleX + 0.5f // You can adjust the scale factor
                itemImageView.scaleX = newScale
                itemImageView.scaleY = newScale
                counter2 += 1
            }
            else{
                val newScale = itemImageView.scaleX - 0.5f // You can adjust the scale factor
                itemImageView.scaleX = newScale
                itemImageView.scaleY = newScale
                counter2 -= 1
            }
        }


        btnBack.setOnClickListener {
            finish()
        }

        itemDescriptionTextView.loadDataWithBaseURL(
            null,
            "<html><body style='text-align:justify;'>$itemDescription</body></html>",
            "text/html",
            "UTF-8",
            null
        )
    }
}