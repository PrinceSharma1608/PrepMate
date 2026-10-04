package com.PrepMate

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuestionSelectionFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("PrepMatePrefs", MODE_PRIVATE)
        val name = prefs.getString("userName", null)
        val welcomeText = view.findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome, $name!"


        val dsaButton = view.findViewById<View>(R.id.dsaButton)

        val dbmsButton = view.findViewById<View>(R.id.dbmsButton)

        val osButton = view.findViewById<View>(R.id.osButton)

        val cnButton = view.findViewById<View>(R.id.cnButton)

        val oopButton = view.findViewById<View>(R.id.oopButton)

        val coaButton = view.findViewById<View>(R.id.coaButton)

        val daaButton = view.findViewById<View>(R.id.daaButton)

        val seButton = view.findViewById<View>(R.id.seButton)


        dsaButton.setOnClickListener {
            openConfig()
        }

        dbmsButton.setOnClickListener {
            openConfig()
        }

        osButton.setOnClickListener {
            openConfig()
        }

        cnButton.setOnClickListener {
            openConfig()
        }

        oopButton.setOnClickListener {
            openConfig()
        }

        coaButton.setOnClickListener {
            openConfig()
        }

        daaButton.setOnClickListener {
            openConfig()
        }

        seButton.setOnClickListener {
            openConfig()
        }
    }

    private fun openConfig() {

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