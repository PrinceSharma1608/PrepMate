package com.PrepMate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestSelection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.second_activity)

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, QuestionSelectionFragment())
                .commit()
        }

    }
}