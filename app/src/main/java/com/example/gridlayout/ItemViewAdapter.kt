package com.example.gridlayout

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.values
import com.squareup.picasso.Picasso

class ItemViewAdapter(var context: Context, private var items: ArrayList<Items>)
    : RecyclerView.Adapter<ItemViewAdapter.CartItemViewHolder>()  {

    private val auth = FirebaseAuth.getInstance()
    private val myDbRef = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.checkout_item, parent, false)
        return CartItemViewHolder(itemView)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = items[position]

        with(holder){
            with(currentItem){

//##################### create code to get total (599*8) = ? ###################################

                cartItemName.text = this.itemName
                Picasso.get().load(this.itemImageUri).into(cartItemImage)
                cartItemPrice.text = this.itemPrice
                cartItemQuantity.text = this.itemQuantity
                cartItemPriceAndQty.text = "x ${this.cartItemPriceAndQuantity}"
                //cartItemTtl.text = "Total: ${((itemPrice.toString().toInt())*(itemQuantity.toString().toInt()))}"


                cartItemRemoveBtn.setOnClickListener {
                    removeItem(currentItem.itemUid, currentItem.itemName)
                }

                cartQuantityDecreaseBtn.setOnClickListener {
                    var cartQuantity = holder.cartItemQuantity.text.toString().toInt()
                    if (cartQuantity < 2){
                        Toast.makeText(context, "Item cannot be less than 1 ", Toast.LENGTH_SHORT).show()
                    }

                    else{
                        cartQuantity--

                        myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                            .child(currentItem.itemUid!!).child("itemQuantity").setValue(cartQuantity.toString())

                        cartItemQuantity.text = cartQuantity.toString()

                        cartItemPriceAndQuantity = "x $cartItemPriceAndQuantity"

                        myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                            .child(currentItem.itemUid!!).child("cartItemPriceAndQuantity")
                            .setValue(cartQuantity.toString())


                    }
                }

                cartQuantityIncreaseBtn.setOnClickListener {
                    var cartQuantity = holder.cartItemQuantity.text.toString().toInt()
                    cartQuantity++

                    myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                        .child(currentItem.itemUid!!).child("itemQuantity").setValue(cartQuantity.toString())

                    holder.cartItemQuantity.text = cartQuantity.toString()
                    cartItemPriceAndQuantity = "x $cartItemPriceAndQuantity"
                    myDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart")
                        .child(currentItem.itemUid!!).child("cartItemPriceAndQuantity")
                        .setValue(cartQuantity.toString())
                }
            }

        }



    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartItemName: TextView = itemView.findViewById(R.id.cartItemName)
        val cartItemImage: ImageView = itemView.findViewById(R.id.cartItemImage)
        val cartItemPrice: TextView = itemView.findViewById(R.id.cartItemPrice)
        val cartItemPriceAndQty: TextView = itemView.findViewById(R.id.cartItemPriceAndQuantity)
        val cartItemRemoveBtn: ImageView = itemView.findViewById(R.id.cartItemRemove)
        val cartQuantityDecreaseBtn: TextView = itemView.findViewById(R.id.cartQuantityDecrease)
        val cartItemQuantity: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val cartQuantityIncreaseBtn: TextView = itemView.findViewById(R.id.cartQuantityIncrease)
        val cartItemLayout :CardView = itemView.findViewById(R.id.cartItemLayout)
        var cartItemTtl :TextView = itemView.findViewById(R.id.cartItemTotal)

    }

    private fun removeItem(itemUid: String?, itemName: String?) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users").child(auth.currentUser?.uid!!).child("Cart").child(itemUid!!)

        mDbRef.removeValue().addOnSuccessListener {
            Toast.makeText(context, "$itemName removed from cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}