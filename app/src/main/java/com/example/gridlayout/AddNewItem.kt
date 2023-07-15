package com.example.gridlayout

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class AddNewItem : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private lateinit var keyList: ArrayList<String>
    //private var itemQuantity: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_item)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        keyList = ArrayList()

        val addItemImage = findViewById<ImageView>(R.id.newItem_image)
        val addItemBtn = findViewById<CardView>(R.id.newItem_addBtnLayout)

        /*val imageRef = storageReference.child("Users").child(auth.currentUser?.uid!!).child("profileImage")
        val localFile = File.createTempFile("tempImage",".jpg")
        Toast.makeText(this,"Loading image", Toast.LENGTH_SHORT).show()
        imageRef.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            addItemImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(this,"Unable to retrieve image", Toast.LENGTH_SHORT).show()
        }*/

        addItemImage.setOnClickListener {
            startFileChooser()
        }

        addItemBtn.setOnClickListener {
            uploadPhotoToFirebase()
        }

    }

    private fun startFileChooser(){
        if(CropImage.isExplicitCameraPermissionRequired(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }
        else {
            startActivityForResult(CropImage.getPickImageChooserIntent(this), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imgUri = CropImage.getPickImageResultUri(this, data)
            CropImage.activity(imgUri)
                .setAspectRatio(1,1)
                .start(this)
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                imageUri = result.uri

                val addItemImage = findViewById<ImageView>(R.id.newItem_image)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
                addItemImage?.setImageBitmap(bitmap)
                //profile_selectOrUpdatePhoto.text = "Save New Image"
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }else{
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val datar = baos.toByteArray()
                val addItemImage = findViewById<ImageView>(R.id.newItem_image)
                addItemImage.setImageBitmap(imageBitmap)
                //storageRef!!.putBytes(datar)
            }
        }

    }

    private fun uploadPhotoToFirebase() {
        if (imageUri!=null){
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading image")
            pd.show()

            val fileName = UUID.randomUUID().toString()+".jpg"
            val imageRef = storageReference.child("Users").child(auth.currentUser?.uid!!)
                .child("itemImage").child(fileName)
            imageRef.putFile(imageUri!!).addOnSuccessListener {p0 ->
                pd.dismiss()

                //profile_selectOrUpdatePhoto.text = "Update Profile Picture"
                Toast.makeText(this, "Image updated successfully", Toast.LENGTH_SHORT).show()
                imageRef.downloadUrl.addOnSuccessListener {
                    addItemToDatabase(it.toString())
                }
            }
                .addOnFailureListener{p0 ->
                    pd.dismiss()
                    Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {p0 ->
                    val progress = (100.0 * p0.bytesTransferred)/p0.totalByteCount
                    pd.setMessage("uploaded ${progress.toInt()}%")

                }
        }
    }

    private fun addItemToDatabase(profileImageUri: String) {

        val itemName = findViewById<EditText>(R.id.newItem_name).text.toString()
        val itemPrice = findViewById<EditText>(R.id.newItem_price).text.toString()
        val description = findViewById<EditText>(R.id.newItem_description).text.toString()
        val quantity = findViewById<EditText>(R.id.newItem_quantity).text.toString()

        val newItem = mDbRef.child("Users").child(auth.currentUser?.uid!!).child("Items").push()
        val itemDetails = Items(newItem.key!!, profileImageUri, itemName, ("Rs. $itemPrice"),
            description,quantity,null,null,null)

        newItem.setValue(itemDetails).addOnSuccessListener {

            Toast.makeText(this, "Item added successfully", Toast.LENGTH_LONG).show()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

}