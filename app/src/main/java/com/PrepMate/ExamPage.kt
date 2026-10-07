package com.PrepMate

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ExamPage : AppCompatActivity() {

    private lateinit var questionNumberText: TextView
    private lateinit var timerText: TextView

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_page)

        questionNumberText = findViewById(R.id.questionNumberText)
        timerText = findViewById(R.id.timerText)

        // Get number of questions
        val totalQuestions = GlobalData.numberOfQuestions

        // Show question count
        questionNumberText.text = "Question 1/$totalQuestions"

        // Check if timer is enabled
        val time =(Integer.parseInt(GlobalData.timer))
        if (time > 0) {
            startTimer(time)
        } else {
            timerText.text = "No Timer"
        }
    }

    private fun startTimer(minutes: Int) {

        val totalTimeMillis = minutes * 60 * 1000L

        countDownTimer = object : CountDownTimer(
            totalTimeMillis,
            1000
        ) {

            override fun onTick(millisUntilFinished: Long) {

                val minutesLeft =
                    millisUntilFinished / 60000

                val secondsLeft =
                    (millisUntilFinished % 60000) / 1000

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