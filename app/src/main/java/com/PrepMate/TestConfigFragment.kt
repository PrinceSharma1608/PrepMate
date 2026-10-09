
package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class TestConfigFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

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
        val questionCountEditText =
            view.findViewById<EditText>(R.id.questionCountEditText)

        // Quiz configuration
        val negativeMarkingSwitch =
            view.findViewById<Switch>(R.id.negativeMarkingSwitch)

        val timerSwitch =
            view.findViewById<Switch>(R.id.timerSwitch)

        val startQuizButton =
            view.findViewById<Button>(R.id.startQuizButton)

        subjectText.text = "You've Chosen : ${GlobalData.subject}"

        // Start Quiz
        startQuizButton.setOnClickListener {

            val questionCountText =
                questionCountEditText.text.toString().trim()

            // Check if empty
            if (questionCountText.isBlank()) {
                questionCountEditText.error =
                    "Enter number of questions"
                return@setOnClickListener
            }

            val questionCount = questionCountText.toIntOrNull()

            // Validate question count
            if (
                questionCount == null ||
                questionCount <= 0 ||
                questionCount > 100
            ) {
                questionCountEditText.error =
                    "Enter a number between 1 and 100"
                return@setOnClickListener
            }

            // Store configuration
            GlobalData.numberOfQuestions = questionCount.toString()
            GlobalData.negativeMarking =
                negativeMarkingSwitch.isChecked

            val time = questionCount / 2

            GlobalData.timer = (
                    if (timerSwitch.isChecked) time else 0
                    ).toString()

            // Prevent repeated clicks while fetching
            startQuizButton.isEnabled = false

            // Fetch questions from Firestore
            fireStore(questionCount, startQuizButton)
        }
    }

    private fun fireStore(
        questionCount: Int,
        startQuizButton: Button
    ) {
        db.collection("questions")
            .whereEqualTo("subject", GlobalData.code)
            .get()
            .addOnSuccessListener { result ->

                val questions = mutableListOf<Question>()

                // Convert Firestore documents into Question objects
                for (document in result) {
                    val question = Question(
                        question = document.getString("question") ?: "",
                        optionA = document.getString("optionA") ?: "",
                        optionB = document.getString("optionB") ?: "",
                        optionC = document.getString("optionC") ?: "",
                        optionD = document.getString("optionD") ?: "",
                        correctAnswer = document.getString("correctAnswer") ?: ""
                    )

                    questions.add(question)
                }

                // Check if questions exist
                if (questions.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "No questions found for this subject!",
                        Toast.LENGTH_LONG
                    ).show()

                    startQuizButton.isEnabled = true
                    return@addOnSuccessListener
                }

                // Check if enough questions are available
                if (questions.size < questionCount) {
                    Toast.makeText(
                        requireContext(),
                        "Only ${questions.size} questions available. Requested: $questionCount",
                        Toast.LENGTH_LONG
                    ).show()

                    startQuizButton.isEnabled = true
                    return@addOnSuccessListener
                }

                // Randomise the questions
                questions.shuffle()

                // Select the requested number of questions
                GlobalData.selectedQuestions = questions
                    .take(questionCount)
                    .toMutableList()

                // Show success Toast
                Toast.makeText(
                    requireContext(),
                    "${GlobalData.selectedQuestions.size} questions fetched successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                // Open ExamPage after fetching questions
                val intent = Intent(
                    requireContext(),
                    ExamPage::class.java
                )

                startActivity(intent)

                // Close current Activity
                requireActivity().finish()
            }
            .addOnFailureListener { exception ->

                // Show error Toast
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch questions: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()

                startQuizButton.isEnabled = true
            }
    }
}
