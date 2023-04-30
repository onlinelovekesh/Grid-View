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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val logLoginBtn = findViewById<CardView>(R.id.logLoginBtn)
        val logUsername = findViewById<TextView>(R.id.logUsername)
        val logGoToRegistration = findViewById<TextView>(R.id.logGoToRegistration)

        if (Firebase.auth.currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }else{

            logLoginBtn.setOnClickListener {

                val email = logUsername.text.toString()
                val pass = findViewById<EditText>(R.id.logPassword).text.toString()

                when {
                    email.isEmpty() -> Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                    pass.isEmpty() -> Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                    else -> signIn(email, pass)
                }
            }
        }

        logUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidEmail(logUsername.text.toString())) {
                    logUsername.error = null
                } else logUsername.error = "Invalid Email"
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        logGoToRegistration.setOnClickListener {
            logGoToRegistration.setTextColor(Color.RED)
            val i = Intent(this, Registration::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun signIn(email: String, pass: String) {

        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.progress_bar)
        val title = dialogue.findViewById<TextView>(R.id.progressTitle)
        title.text = "Please wait"
        val message = dialogue.findViewById<TextView>(R.id.progressMessage)
        message.text = "Verifying user"
        dialogue.show()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                dialogue.dismiss()
                //Toast.makeText(this, "Welcome user!!", Toast.LENGTH_SHORT).show()
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
            } else {
                dialogue.dismiss()
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}