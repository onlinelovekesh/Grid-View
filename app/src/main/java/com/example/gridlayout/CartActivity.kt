package com.example.gridlayout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CartActivity : AppCompatActivity() {

    private lateinit var myDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var items: ArrayList<Items>
    private lateinit var adapter: ItemViewAdapter
    private lateinit var itemRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        auth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        items = ArrayList()
        adapter = ItemViewAdapter(this, items)
        itemRecyclerView = findViewById(R.id.cartItemView)
        val layoutManager = LinearLayoutManager(this)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = adapter

        getCartItems()

    }

    private fun getCartItems() {

        myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
            .addValueEventListener(object : ValueEventListener{

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    items.clear()

                    for (eachItem in snapshot.children){
                        val allItems = eachItem.getValue(Items::class.java)
                        items.add(allItems!!)
                        //progressBar.visibility = View.GONE

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) { }

            })

    }
}