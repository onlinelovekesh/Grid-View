package com.example.gridlayout

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(var context: Context, private var itemList: ArrayList<Items>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.grid_view, parent, false)
        return ItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

//################# show image, name, age gender in home page(available users) list view ###################################

        Picasso.get().load(currentItem.itemImageUri).into(holder.itemImage)
        holder.itemName.text = currentItem.itemName
        holder.itemPrice.text = currentItem.itemPrice

        holder.gridViewItem.setOnClickListener {
            val i = Intent(context, ItemView::class.java)
            i.putExtra("itemName", currentItem.itemName)
            i.putExtra("itemUid", currentItem.itemUid)
            i.putExtra("itemImage",currentItem.itemImageUri)
            i.putExtra("itemPrice",currentItem.itemPrice)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val gridViewItem : CardView = itemView.findViewById(R.id.gridView_item)
    }
}
