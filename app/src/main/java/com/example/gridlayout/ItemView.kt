package com.example.gridlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ItemView : AppCompatActivity() {

    private lateinit var myDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private var itemDescription: String? = null
    //private var itemQuantity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        auth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference


        val itemName = intent.getStringExtra("itemName").toString() //from RecyclerViewAdaptor
        val itemUid = intent.getStringExtra("itemUid").toString()
        val itemImage = intent.getStringExtra("itemImage")
        val itemPrice = intent.getStringExtra("itemPrice").toString()

        val itemViewName = findViewById<TextView>(R.id.itemView_name)
        val itemViewPrice = findViewById<TextView>(R.id.itemView_price)
        val itemViewImage = findViewById<ImageView>(R.id.itemView_image)
        val itemViewDescription = findViewById<TextView>(R.id.itemView_description)

        itemViewName.text = itemName
        itemViewPrice.text = itemPrice

        myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items")
            .child(itemUid).get().addOnSuccessListener {
                if (it.exists()){
                    itemDescription = it.child("itemDescription").value as String?
                    itemViewDescription.text = itemDescription.toString()
                }
                else{
                    Toast.makeText(this, "Unable to load data, please try again", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load data, please try again", Toast.LENGTH_SHORT).show()
            }


        //itemViewDescription.text = itemDescription.toString()
        Picasso.get().load(itemImage).into(itemViewImage)

//############################################## Add item to cart ###########################################

        val addToCart = findViewById<CardView>(R.id.itemView_addToCartBtnLayout)

        addToCart.setOnClickListener {

            val cartItemPath = myDbRef.child("Users").child(auth.currentUser?.uid!!)
                .child("Cart").child(itemUid)
            val itemDetails = Items(itemUid, itemImage!!, itemName, itemPrice,itemDescription!!,"1")

            cartItemPath.setValue(itemDetails).addOnSuccessListener {

                Toast.makeText(this, "Item added to cart", Toast.LENGTH_LONG).show()
            }

        }

//################################################ Go to cart page ##########################################
        val cart = findViewById<ImageView>(R.id.itemView_cartBtn)

        cart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
//###########################################################################################################

        }


    }
