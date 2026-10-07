package com.PrepMate

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuestionSelectionFragment : Fragment() {

    // Class-level variable so openConfig() can access it
    private var subject = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_question_selection,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity()
            .getSharedPreferences("PrepMatePrefs", MODE_PRIVATE)

        val name = prefs.getString("userName", null)

        val welcomeText = view.findViewById<TextView>(
            R.id.welcomeText
        )

        welcomeText.text = "Welcome, $name!"

        // Buttons
        val dsaButton = view.findViewById<LinearLayout>(R.id.dsaButton)
        val dbmsButton = view.findViewById<LinearLayout>(R.id.dbmsButton)
        val osButton = view.findViewById<LinearLayout>(R.id.osButton)
        val cnButton = view.findViewById<LinearLayout>(R.id.cnButton)
        val oopButton = view.findViewById<LinearLayout>(R.id.oopButton)
        val coaButton = view.findViewById<LinearLayout>(R.id.coaButton)
        val daaButton = view.findViewById<LinearLayout>(R.id.daaButton)
        val seButton = view.findViewById<LinearLayout>(R.id.seButton)

        // DSA
        dsaButton.setOnClickListener {
            subject = "Data Structures and Algorithms"
            openConfig()
        }

        // DBMS
        dbmsButton.setOnClickListener {
            subject = "Database Management Systems"
            openConfig()
        }

        // OS
        osButton.setOnClickListener {
            subject = "Operating Systems"
            openConfig()
        }

        // CN
        cnButton.setOnClickListener {
            subject = "Computer Networks"
            openConfig()
        }

        // OOP
        oopButton.setOnClickListener {
            subject = "Object Oriented Programming"
            openConfig()
        }

        // COA
        coaButton.setOnClickListener {
            subject = "Computer Organization and Architecture"
            openConfig()
        }

        // DAA
        daaButton.setOnClickListener {
            subject = "Design and Analysis of Algorithms"
            openConfig()
        }

        // Software Engineering
        seButton.setOnClickListener {
            subject = "Software Engineering"
            openConfig()
        }
    }

    private fun openConfig() {

        GlobalData.subject = subject

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainerView,
                TestConfigFragment()
            )
            .addToBackStack(null)
            .commit()
    }
}