package com.example.gridlayout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class Registration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        myDbRef = FirebaseDatabase.getInstance().reference
        storageRef = Firebase.storage.reference

        val regEmail = findViewById<TextView>(R.id.regEmail)
        val regSubmitBtn = findViewById<CardView>(R.id.regSubmitBtn)
        val regGoToLogin = findViewById<TextView>(R.id.regGoToLogin)

        regEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidEmail(regEmail.text.toString())) {
                    regEmail.error = null
                } else regEmail.error = "Invalid Email"
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        regSubmitBtn.setOnClickListener {

            val name = findViewById<EditText>(R.id.regName).text.toString()
            val email = regEmail.text.toString()
            val password = findViewById<EditText>(R.id.regPassword).text.toString()
            val cnfPassword = findViewById<EditText>(R.id.regCnfPassword).text.toString()

            when {
                name.isBlank() -> Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                email.isBlank() -> Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                password.isBlank() -> Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                cnfPassword.isBlank() -> Toast.makeText(this, "Please enter Confirm password", Toast.LENGTH_SHORT).show()
                else -> signUp(name, email, cnfPassword)

            }
        }

        regGoToLogin.setOnClickListener {
            regGoToLogin.setTextColor(Color.RED)
            val i = Intent(this, Login::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun signUp(name: String, email: String, password: String) {

        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.progress_bar)
        val title = dialogue.findViewById<TextView>(R.id.progressTitle)
        title.text = "Please wait"
        val message = dialogue.findViewById<TextView>(R.id.progressMessage)
        message.text = "Registration is in progress"
        dialogue.show()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                addUserToDatabase(name, email, password)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            //title.text = "Uh-oh"
            //message.text = "Error ${it.message}"
            dialogue.dismiss()

        }

    }

    private fun addUserToDatabase(name: String, email: String, pass: String) {
        val userDetails = User(auth.currentUser?.uid!!, name, email, pass)
        myDbRef.child("Users").child(auth.currentUser?.uid!!).setValue(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "User added to database", Toast.LENGTH_SHORT).show()

                val i = Intent(this, Login::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)

            }.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}