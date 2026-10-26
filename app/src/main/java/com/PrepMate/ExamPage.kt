package com.PrepMate

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.PrepMate.data.local.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ExamPage : AppCompatActivity() {

    private var count = 1
    private var score = 0
    private var finished = false

    private lateinit var questionNumberText: TextView
    private lateinit var timerText: TextView
    private lateinit var nxt: Button
    private lateinit var questionText: TextView
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton
    private lateinit var optionsGroup: RadioGroup
    private lateinit var bookmarkButton: TextView

    private val totalQuestions = GlobalData.numberOfQuestions.toInt()

    private var countDownTimer: CountDownTimer? = null
    private var qid: String = ""

    // SCORE POPUP
    private fun showScorePopup(score: Int, totalQuestions: Int) {
        AlertDialog.Builder(this)
            .setTitle("Exam Completed!")
            .setMessage("Your Score: $score / $totalQuestions. \nVisit Attempt Analysis to see detailed analysis")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, TestSelection::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }


    private fun qGetter() {
        val question = GlobalData.selectedQuestions[count - 1]

        qid = question.questionID

        GlobalData.question = question.question
        GlobalData.optionA = question.optionA
        GlobalData.optionB = question.optionB
        GlobalData.optionC = question.optionC
        GlobalData.optionD = question.optionD
        GlobalData.correctAnswer = question.correctAnswer

        questionText.text = GlobalData.question
        optionA.text = GlobalData.optionA
        optionB.text = GlobalData.optionB
        optionC.text = GlobalData.optionC
        optionD.text = GlobalData.optionD

        bookmarkButton.text = "  Bookmark this question   "
        lifecycleScope.launch {
            val isBookmarked = PrepMateDatabase.getDatabase(this@ExamPage).bookmarkDao().isBookmarked(qid)
            bookmarkButton.text = if (isBookmarked) "  Bookmarked !   " else "  Bookmark this question   "
        }

        val selectedOption =
            GlobalData.attemptResponses[qid]?.selectedOption

        val selectedButton = when (selectedOption) {
            GlobalData.optionA -> optionA
            GlobalData.optionB -> optionB
            GlobalData.optionC -> optionC
            GlobalData.optionD -> optionD
            else -> null
        }

        optionsGroup.setOnCheckedChangeListener(null)
        optionsGroup.clearCheck()

        selectedButton?.isChecked = true

        optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val selectedRadioButton =
                    group.findViewById<RadioButton>(checkedId)

                GlobalData.attemptResponses[qid]?.selectedOption =
                    selectedRadioButton.text.toString()
            }
        }
    }

    // BOOKMARK QUESTION
    private fun bookmarkQ() {
        lifecycleScope.launch {
            try {
                val db =
                    PrepMateDatabase.getDatabase(this@ExamPage)

                val bookmarkedQuestion = BookmarkedQuestion(
                    qid = qid,
                    question = GlobalData.question,
                    optionA = GlobalData.optionA,
                    optionB = GlobalData.optionB,
                    optionC = GlobalData.optionC,
                    optionD = GlobalData.optionD,
                    correctAnswer = GlobalData.correctAnswer
                )

                db.bookmarkDao().addBookmark(bookmarkedQuestion)
                bookmarkButton.text = "  Bookmarked !   "

                Toast.makeText(
                    this@ExamPage,
                    "Question bookmarked",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@ExamPage,
                    "Bookmark failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // CHECK ANSWER AND UPDATE SCORE
    private fun checker() {
        val selectedId = optionsGroup.checkedRadioButtonId

        if (selectedId == -1) return

        val selectedRadioButton =
            findViewById<RadioButton>(selectedId)

        val selectedAnswer =
            selectedRadioButton.text.toString()

        if (selectedAnswer == GlobalData.correctAnswer) {
            score++
        } else if (GlobalData.negativeMarking) {
            score--
        }
    }

    // FINISH QUIZ
    private fun finishQuiz() {
        if (finished) return

        finished = true
        nxt.isEnabled = false
        countDownTimer?.cancel()

        // Check the last question
        checker()

        // Save attempt to Firestore and Room
        submitAttempt()

        // Display final score
        showScorePopup(score, totalQuestions)
    }

    // SAVE ATTEMPT
    private fun submitAttempt() {
        val timestamp = System.currentTimeMillis()

        val responses = GlobalData.attemptResponses.values.map {
            mapOf(
                "questionId" to it.questionId,
                "question" to it.question,
                "correctOption" to it.correctOption,
                "selectedOption" to it.selectedOption
            )
        }

        // Firestore contains the responses array
        val attemptData = hashMapOf(
            "responses" to responses
        )

        FirebaseFirestore.getInstance()
            .collection("Attemps")
            .add(attemptData)
            .addOnSuccessListener { documentReference ->

                val firestoreId = documentReference.id

                lifecycleScope.launch {
                    try {
                        val db =
                            PrepMateDatabase.getDatabase(this@ExamPage)

                        val attempt = QuizAttempt(
                            firestoreId = firestoreId,
                            timestamp = timestamp,
                            score = score
                        )

                        db.bookmarkDao().insertAttempt(attempt)

                        GlobalData.attemptResponses.clear()

                        Toast.makeText(
                            this@ExamPage,
                            "Attempt saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ExamPage,
                            "Room save failed: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Firestore save failed: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    // ACTIVITY SETUP
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_page)

        questionNumberText =
            findViewById(R.id.questionNumberText)

        timerText =
            findViewById(R.id.timerText)

        nxt =
            findViewById(R.id.nextButton)

        questionText =
            findViewById(R.id.questionText)

        optionA =
            findViewById(R.id.optionA)

        optionB =
            findViewById(R.id.optionB)

        optionC =
            findViewById(R.id.optionC)

        optionD =
            findViewById(R.id.optionD)

        optionsGroup =
            findViewById(R.id.optionsGroup)

        bookmarkButton =
            findViewById(R.id.textView2)

        // Load first question
        qGetter()

        questionNumberText.text =
            "Question $count/$totalQuestions"

        nxt.text =
            if (totalQuestions == 1) "Finish" else "Next"

        // BOOKMARK BUTTON
        bookmarkButton.setOnClickListener {
            bookmarkQ()
            bookmarkButton.text = "  Bookmarked !   "
        }

        // TIMER
        val time = GlobalData.timer.toInt()

        if (time > 0) {
            startTimer(time)
        } else {
            timerText.text = "No Timer"
        }

        // NEXT / FINISH BUTTON
        nxt.setOnClickListener {
            if (finished) return@setOnClickListener

            if (count < totalQuestions) {

                checker()

                count++

                qGetter()

                questionNumberText.text =
                    "Question $count/$totalQuestions"

                nxt.text =
                    if (count == totalQuestions) "Finish"
                    else "Next"

            } else {
                finishQuiz()
            }
        }
    }

    // COUNTDOWN TIMER
    private fun startTimer(minutes: Int) {
        val totalTimeMillis = minutes * 60 * 1000L

        countDownTimer =
            object : CountDownTimer(totalTimeMillis, 1000) {

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

                    if (minutesLeft == 1L && secondsLeft == 0L) {
                        timerText.setTextColor(Color.RED)
                    }
                }

                override fun onFinish() {
                    timerText.text = "00:00"
                    finishQuiz()
                }
            }.start()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}
