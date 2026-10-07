package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment

class TestConfigFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_test_config,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Selected subject
        val subjectText = view.findViewById<TextView>(R.id.subjectText)
        // Number of questions
        val questionCountEditText = view.findViewById<EditText>(R.id.questionCountEditText)

        // Negative marking
        val negativeMarkingSwitch = view.findViewById<Switch>(R.id.negativeMarkingSwitch)

        // Timer
        val timerSwitch = view.findViewById<Switch>(R.id.timerSwitch)

        // Start button
        val startQuizButton = view.findViewById<Button>(R.id.startQuizButton)

        // Display selected subject
        subjectText.text = "You've Chosen : ${GlobalData.subject}"


        // Start Quiz
        startQuizButton.setOnClickListener {

            val questionCountText =
                questionCountEditText.text.toString().trim()


            // Check if empty
            if (questionCountText.isEmpty()) {

                questionCountEditText.error =
                    "Enter number of questions"

                return@setOnClickListener
            }


            // Convert to Integer
            val questionCount =
                questionCountText.toIntOrNull()


            // Check if between 1 and 100
            if (
                questionCount == null ||
                questionCount <= 0 ||
                questionCount > 100
            ) {

                questionCountEditText.error =
                    "Enter a number between 1 and 100"

                return@setOnClickListener
            }


            // Store configuration in GlobalData
            GlobalData.numberOfQuestions = questionCount.toString()

            GlobalData.negativeMarking =
                negativeMarkingSwitch.isChecked.toString()
            val time =(Integer.parseInt(GlobalData.numberOfQuestions)/2).toString()
            GlobalData.timer =
                (if (timerSwitch.isChecked) time else 0).toString()


            // Start ExamPage
            val intent = Intent(
                requireContext(),
                ExamPage::class.java
            )

            startActivity(intent)


            // Close current Activity
            requireActivity().finish()
        }
    }
}