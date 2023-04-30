package com.example.gridlayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gridlayout.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference
    private lateinit var itemList: ArrayList<Items>
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().reference
        itemList = ArrayList()
        adapter = RecyclerViewAdapter(this, itemList)
        itemRecyclerView = findViewById(R.id.itemView)
        val layoutManager = GridLayoutManager(this, 2)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = adapter

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        binding.heading.setOnClickListener {
            startActivity(Intent(this,AddNewItem::class.java))
        }

        getItems(progressBar)
    }

    private fun getItems(progressBar: ProgressBar) {

        myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items")
            .addValueEventListener(object : ValueEventListener{

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    itemList.clear()

                    for (eachItem in snapshot.children){
                        val allItems = eachItem.getValue(Items::class.java)
                        itemList.add(allItems!!)
                        progressBar.visibility = View.GONE

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) { }

            })
        //progressBar.visibility = View.INVISIBLE

    }
}
