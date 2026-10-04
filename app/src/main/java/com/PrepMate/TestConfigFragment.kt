package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
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

        val subjectText =
            view.findViewById<TextView>(R.id.subjectText)

        val questionCountSpinner =
            view.findViewById<Spinner>(R.id.questionCountSpinner)

        val negativeMarkingSwitch =
            view.findViewById<Switch>(R.id.negativeMarkingSwitch)

        val timerSwitch =
            view.findViewById<Switch>(R.id.timerSwitch)

        val startQuizButton =
            view.findViewById<Button>(R.id.startQuizButton)

        // Show selected subject
        subjectText.text = subject

        startQuizButton.setOnClickListener {

            val selectedPosition =
                questionCountSpinner.selectedItemPosition

            val questionCount =
                when (selectedPosition) {
                    0 -> 10
                    1 -> 20
                    2 -> 30
                    3 -> 50
                    4 -> 100
                    else -> 10
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

            intent.putExtra("subject", subject)
            intent.putExtra("questionCount", questionCount)
            intent.putExtra(
                "negativeMarking",
                negativeMarking
            )
            intent.putExtra(
                "timerEnabled",
                timerEnabled
            )

            startActivity(intent)

            // Don't keep configuration screen in back stack
            requireActivity().finish()
        }
    }

    companion object {

        fun newInstance(subject: String):
                TestConfigFragment {

            val fragment = TestConfigFragment()

            val bundle = Bundle()
            bundle.putString("subject", subject)

            fragment.arguments = bundle

            return fragment
        }
    }
}