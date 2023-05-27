package com.example.gridlayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ItemViewAdapter(var context: Context, private var items: ArrayList<Items>)
    : RecyclerView.Adapter<ItemViewAdapter.CartItemViewHolder>()  {

    val auth = FirebaseAuth.getInstance()
    val myDbRef = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.checkout_item, parent, false)
        return CartItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = items[position]
        var cartQuantity = 1

        Picasso.get().load(currentItem.itemImageUri).into(holder.cartItemImage)
        holder.cartItemName.text = currentItem.itemName
        holder.cartItemPrice.text = currentItem.itemPrice

        holder.cartItemRemoveBtn.setOnClickListener {
            removeItem(currentItem.itemUid, currentItem.itemName)
        }
        holder.cartQuantityDecrease.setOnClickListener {
            if (cartQuantity < 2){
                Toast.makeText(context, "Item cannot be less than 1 ", Toast.LENGTH_SHORT).show()
            }
            else{
                cartQuantity--
                holder.cartItemQuantity.setText(cartQuantity.toString())

                //myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                    //.child(currentItem.itemUid!!).child("itemQuantity").setValue(cartQuantity.toString())
            }
        }
        holder.cartQuantityIncrease.setOnClickListener {
            cartQuantity++
            holder.cartItemQuantity.setText(cartQuantity.toString())

            myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                .child(currentItem.itemUid!!).child("itemQuantity").setValue(cartQuantity.toString())
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartItemName: TextView = itemView.findViewById(R.id.cartItemName)
        val cartItemImage: ImageView = itemView.findViewById(R.id.cartItemImage)
        val cartItemPrice: TextView = itemView.findViewById(R.id.cartItemPrice)
        val cartItemRemoveBtn: ImageView = itemView.findViewById(R.id.cartItemRemove)
        val cartQuantityDecrease: TextView = itemView.findViewById(R.id.cartQuantityDecrease)
        var cartItemQuantity: EditText = itemView.findViewById(R.id.cartItemQuantity)
        val cartQuantityIncrease: TextView = itemView.findViewById(R.id.cartQuantityIncrease)
    }

    private fun removeItem(itemUid: String?, itemName: String?) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users").child(auth.currentUser?.uid!!).child("cart").child(itemUid!!)

        mDbRef.removeValue().addOnSuccessListener {
            Toast.makeText(context, "$itemName removed from cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}