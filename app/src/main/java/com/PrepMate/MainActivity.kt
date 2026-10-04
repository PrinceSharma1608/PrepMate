package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("PrepMatePrefs", MODE_PRIVATE)
        val savedName = prefs.getString("userName", null)

        if (savedName != null) {
            startActivity(Intent(this, TestSelection::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)
            val nameEditText = findViewById<EditText>(R.id.nameEditText)
            val btn = findViewById<Button>(R.id.startButton)
            btn.setOnClickListener {
                if (nameEditText.text.isBlank()){
                    Toast.makeText(this, "Please enter your name to continue", Toast.LENGTH_SHORT).show()
                return@setOnClickListener}git
                val name = nameEditText.text.toString().trim()
                if (name.isNotEmpty()) {
                    prefs.edit()
                        .putString("userName", name)
                        .apply()
                    val intent = Intent(this, TestSelection::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}