package com.example.gridlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference
    private lateinit var itemList: ArrayList<RecyclerViewModal>
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var itemRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().reference
        itemList = ArrayList()
        adapter = RecyclerViewAdapter(this, itemList)
        itemRecyclerView = findViewById(R.id.idRVCourses)
        val layoutManager = GridLayoutManager(this, 2)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = adapter

        itemList.add(RecyclerViewModal("Android Development", R.drawable.error))
        itemList.add(RecyclerViewModal("C++ Development", R.drawable.error))
        itemList.add(RecyclerViewModal("Java Development", R.drawable.error))
        itemList.add(RecyclerViewModal("Python Development", R.drawable.error))
        itemList.add(RecyclerViewModal("JavaScript Development", R.drawable.error))

        adapter.notifyDataSetChanged()

    }
}
