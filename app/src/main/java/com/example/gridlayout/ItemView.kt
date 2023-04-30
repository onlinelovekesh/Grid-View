package com.example.gridlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ItemView : AppCompatActivity() {

    private lateinit var myDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

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
                    val itemDescription = it.child("itemDescription").value
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



        }


    }
