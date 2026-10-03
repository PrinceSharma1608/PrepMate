package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
      /*  ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        val prefs = getSharedPreferences("PrepMatePrefs", MODE_PRIVATE)
        val savedName = prefs.getString("userName", null)

        if (savedName != null) {
            // Existing user → directly go to second activity
            startActivity(Intent(this, TestSelection::class.java))
            finish()
        } else {
            // New user → show MainActivity
            setContentView(R.layout.activity_main)
            val name = findViewById<EditText>(R.id.nameEditText).toString().trim()
            val btn = findViewById<Button>(R.id.startButton)
            btn.setOnClickListener {
                prefs.edit()
                    .putString("userName", name)
                    .apply()
                intent = Intent(this, TestSelection::class.java)
                startActivity(intent)
            }
        }
    }

    }
