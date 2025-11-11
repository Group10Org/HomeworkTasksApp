package com.example.hwtasks.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hwtasks.ui.MainActivity
import com.example.hwtasks.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFullName = findViewById(R.id.etFullName)
        etEmail    = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhone    = findViewById(R.id.etPhone)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        btnRegister.setOnClickListener { doRegister() }
        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun doRegister() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString()

        if (name.isEmpty()) { toast("Enter your name"); return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { toast("Enter a valid email"); return }
        if (pass.length < 6) { toast("Password must be at least 6 characters"); return }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { t ->
            if (t.isSuccessful) {
                // Optional: update display name
                auth.currentUser?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                )
                // Optional: email verify
                auth.currentUser?.sendEmailVerification()
                toast("Account created. Verification email sent.")
                goToMain()
            } else toast(t.exception?.localizedMessage ?: "Registration failed")
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
