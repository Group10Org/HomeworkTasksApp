package com.example.hwtasks.ui

import android.os.Bundle
import android.content.Context
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hwtasks.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if user is already logged in
        if (savedInstanceState == null) {
            val sharedPref = getSharedPreferences("HwTasksPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

            if (!isLoggedIn) {
                // Show login screen
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, LoginFragment())
                    .commit()
            } else {
                // Show main task screen
                // TODO: Replace with your main fragment
            }
        }
    }
}