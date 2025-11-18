package com.example.hwtasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hwtasks.R
import com.example.hwtasks.databinding.ActivityMainBinding
import com.example.hwtasks.CreationFragment
import com.example.hwtasks.ProfileFragment
import com.example.hwtasks.SettingsFragment
import com.example.hwtasks.StreamFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser

        // User not logged in → send to login
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Action Bar welcome text
        supportActionBar?.title = "Welcome, ${user.email}"

        // Load default screen (Stream)
        loadFragment(StreamFragment())

        // Bottom navigation listener
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_stream -> loadFragment(StreamFragment())
                R.id.nav_creation -> loadFragment(CreationFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
                R.id.nav_settings -> loadFragment(SettingsFragment())

            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    // Add Sign Out menu item
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle ActionBar clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

