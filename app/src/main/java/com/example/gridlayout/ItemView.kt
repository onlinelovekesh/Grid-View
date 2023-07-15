package com.example.gridlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.gridlayout.databinding.ActivityItemViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ItemView : AppCompatActivity() {

    private lateinit var binding: ActivityItemViewBinding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private var itemDescription: String? = null
    private var isFavorite: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_view)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        val itemName = intent.getStringExtra("itemName").toString() //from RecyclerViewAdaptor
        val itemUid = intent.getStringExtra("itemUid").toString()
        val itemImage = intent.getStringExtra("itemImage")
        val itemPrice = intent.getStringExtra("itemPrice").toString()
        val itemQuantity = intent.getStringExtra("itemQuantity").toString()

        binding.itemViewName.text = itemName
        binding.itemViewPrice.text = itemPrice
        binding.itemViewQuantity.text = itemQuantity
        Picasso.get().load(itemImage).into(binding.itemViewImage)
        binding.itemViewFavoriteBtn.setImageResource(R.drawable.favorite_icon_blank)

        mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items")
            .child(itemUid).get().addOnSuccessListener {
                if (it.exists()) {
                    itemDescription = it.child("itemDescription").value as String?
                    binding.itemViewDescription.text = itemDescription.toString()

                    isFavorite = it.child("favorite").value as String?
                    if (isFavorite == "Yes"){
                        binding.itemViewFavoriteBtn.setImageResource(R.drawable.favorite_icon_filled)
                    }else{
                        binding.itemViewFavoriteBtn.setImageResource(R.drawable.favorite_icon_blank)
                    }

                } else {
                    Toast.makeText(this, "Data not exists", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load data, please try again", Toast.LENGTH_SHORT)
                    .show()
            }



//############################################## Add item to cart ###########################################

        binding.itemViewAddToCartBtnLayout.setOnClickListener {
            addToCart(itemUid, itemImage,itemName,itemPrice,itemQuantity,isFavorite)
        }

//############################################## Add item to favorites ###########################################
            binding.itemViewFavoriteBtn.setOnClickListener {

                mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items")
                    .child(itemUid).get().addOnSuccessListener {
                        if (it.exists()) {
                            isFavorite = it.child("favorite").value as String?
                            if (isFavorite == "Yes"){
                                mDbRef.child("Users").child(auth.currentUser?.uid!!).child("WishList")
                                    .child(itemUid).removeValue().addOnSuccessListener{

                                        val itemDetails = Items(itemUid, itemImage!!, itemName, itemPrice, itemDescription!!, itemQuantity,
                                            null, null,"No")

                                        mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items").child(itemUid)
                                            .setValue(itemDetails).addOnSuccessListener {

                                                binding.itemViewFavoriteBtn.setImageResource(R.drawable.favorite_icon_blank)
                                                Toast.makeText(this, "Item removed from wishlist", Toast.LENGTH_LONG).show()

                                            }. addOnFailureListener {
                                                Toast.makeText(this, "Not updated in child(Items)", Toast.LENGTH_LONG).show()
                                            }

                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Error! Try again", Toast.LENGTH_LONG).show()
                                    }
                            }else{
                                val itemPath = mDbRef.child("Users").child(auth.currentUser?.uid!!).child("WishList")
                                    .child(itemUid)
                                val itemDetails = Items(itemUid, itemImage!!, itemName, itemPrice, itemDescription!!, itemQuantity,
                                    null, null, "Yes")

                                itemPath.setValue(itemDetails).addOnSuccessListener {

                                    mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items").child(itemUid)
                                        .setValue(itemDetails).addOnSuccessListener {
                                            Toast.makeText(this, "Item added to wishlist", Toast.LENGTH_SHORT).show()

                                            binding.itemViewFavoriteBtn.setImageResource(R.drawable.favorite_icon_filled)

                                        }.addOnFailureListener {
                                            Toast.makeText(this, "Not updated in child(Items)", Toast.LENGTH_LONG).show()
                                        }
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Try adding item to wishlist again", Toast.LENGTH_LONG).show()
                                }
                            }

                        } else {
                            Toast.makeText(this, "Data not exists", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to load data, please try again", Toast.LENGTH_SHORT)
                            .show()
                    }

                }


//################################################ Go to cart page ##########################################

        binding.itemViewCartBtn.setOnClickListener {
                startActivity(Intent(this, CartActivity::class.java))
            }

//############################################## Remove item from favorites ###########################################



    }
//######################################################################################################################

    private fun addToCart(itemUid: String, itemImage: String?, itemName: String, itemPrice: String, itemQuantity: String,
        isFavorite: String?) {

        val itemPath = mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Cart").child(itemUid)

        val itemDetails = Items(itemUid, itemImage!!, itemName, itemPrice, itemDescription!!, itemQuantity,
            "1", null, isFavorite)

        itemPath.setValue(itemDetails).addOnSuccessListener {

            Toast.makeText(this, "Item added to cart", Toast.LENGTH_LONG).show()
            binding.itemViewAddToCartBtn.text = "Go to Cart"
        }
    }

}


