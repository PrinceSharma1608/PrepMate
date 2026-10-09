
package com.PrepMate

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ExamPage : AppCompatActivity() {

    private var count = 1
    private var score =0;
    private lateinit var questionNumberText: TextView
    private lateinit var timerText: TextView
    private lateinit var nxt: Button
    private lateinit var questionText: TextView
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton

    private var countDownTimer: CountDownTimer? = null

    private fun qGetter() {

        val Question = GlobalData.selectedQuestions[count - 1]

        GlobalData.question = Question.question
        GlobalData.optionA = Question.optionA
        GlobalData.optionB = Question.optionB
        GlobalData.optionC = Question.optionC
        GlobalData.optionD = Question.optionD
        GlobalData.correctAnswer = Question.correctAnswer

        questionText.text = GlobalData.question

        optionA.text = GlobalData.optionA
        optionB.text = GlobalData.optionB
        optionC.text = GlobalData.optionC
        optionD.text = GlobalData.optionD
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_page)

        questionNumberText = findViewById(R.id.questionNumberText)
        timerText = findViewById(R.id.timerText)
        nxt = findViewById(R.id.nextButton)

        questionText = findViewById(R.id.questionText)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        val optionsGroup = findViewById<RadioGroup>(R.id.optionsGroup)

        qGetter()
        fun checker() {
            val selectedId = optionsGroup.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val selectedAnswer = selectedRadioButton.text.toString()
                if (selectedAnswer == GlobalData.correctAnswer) {
                    score++
                } else if (GlobalData.negativeMarking) {
                    score--
                } else {
                    score += 0
                }
            }
        }
        fun fini() {

        }
        // Get number of questions
        val totalQuestions = GlobalData.numberOfQuestions

        // Show question count
        val stCount = count.toString()
        questionNumberText.text = "Question  $stCount/$totalQuestions"

        // Check if timer is enabled
        val time = Integer.parseInt(GlobalData.timer)

        if (time > 0) {
            startTimer(time)
        } else {
            timerText.text = "No Timer"
        }
        nxt.text = if (totalQuestions.toInt() == 1) "Finish" else "Next"
        nxt.setOnClickListener {

            checker()

            if (count < totalQuestions.toInt()) {

                count++
                qGetter()

                optionsGroup.clearCheck()

                questionNumberText.text = "Question $count/$totalQuestions"

                if (count == totalQuestions.toInt()) {
                    nxt.text = "Finish"
                }

            } else {

                nxt.isEnabled = false
                fini()

            }
        }
    }

    private fun startTimer(minutes: Int) {

        val totalTimeMillis = minutes * 60 * 1000L

        countDownTimer = object : CountDownTimer(totalTimeMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                val minutesLeft = millisUntilFinished / 60000
                val secondsLeft = (millisUntilFinished % 60000) / 1000

                timerText.text = String.format(
                    "%02d:%02d",
                    minutesLeft,
                    secondsLeft
                )
            }
            override fun onFinish() {
                timerText.text = "00:00"

                // Later you can automatically submit the quiz here
            }

        }.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}