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

    private var subject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subject = arguments?.getString("subject") ?: ""
    }

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Selected subject
        val subjectText =
            view.findViewById<TextView>(R.id.subjectText)

        // Number of questions
        val questionCountEditText =
            view.findViewById<EditText>(R.id.questionCountEditText)

        // Negative marking
        val negativeMarkingSwitch =
            view.findViewById<Switch>(R.id.negativeMarkingSwitch)

        // Timer
        val timerSwitch =
            view.findViewById<Switch>(R.id.timerSwitch)

        // Start button
        val startQuizButton =
            view.findViewById<Button>(R.id.startQuizButton)


        // Show selected subject
        subjectText.text = subject


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

            val questionCount =
                questionCountText.toIntOrNull()

            // Check if valid number
            if (questionCount == null || questionCount <= 0) {

                questionCountEditText.error =
                    "Enter a valid number"

                return@setOnClickListener
            }


            val negativeMarking =
                negativeMarkingSwitch.isChecked

            val timerEnabled =
                timerSwitch.isChecked


            // Send configuration to ExamPage
            val intent = Intent(
                requireContext(),
                ExamPage::class.java
            )

            intent.putExtra(
                "subject",
                subject
            )

            intent.putExtra(
                "questionCount",
                questionCount
            )

            intent.putExtra(
                "negativeMarking",
                negativeMarking
            )

            intent.putExtra(
                "timerEnabled",
                timerEnabled
            )


            startActivity(intent)

            // Close TestSelection activity
            requireActivity().finish()
        }
    }


    companion object {

        fun newInstance(
            subject: String
        ): TestConfigFragment {

            val fragment = TestConfigFragment()

            val bundle = Bundle()

            bundle.putString(
                "subject",
                subject
            )

            fragment.arguments = bundle

            return fragment
        }
    }
}