package com.PrepMate

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AttemptDetailListAdapter(
    private val context: Context,
    private val responses: List<Map<String, Any>>
) : BaseAdapter() {

    override fun getCount(): Int = responses.size

    override fun getItem(position: Int): Any = responses[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_attempt_question_detail, parent, false)

        val resp = responses[position]
        val question = resp["question"] as? String ?: ""
        val selectedOption = resp["selectedOption"] as? String ?: "Not Answered"
        val correctOption = resp["correctOption"] as? String ?: ""

        val qText = view.findViewById<TextView>(R.id.detailQuestionText)
        val selectedText = view.findViewById<TextView>(R.id.detailSelectedOptionText)
        val correctText = view.findViewById<TextView>(R.id.detailCorrectOptionText)

        qText.text = "Q${position + 1}: $question"
        selectedText.text = "Your Answer: $selectedOption"
        correctText.text = "Correct Answer: $correctOption"

        if (selectedOption == correctOption) {
            selectedText.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            selectedText.setTextColor(Color.parseColor("#C62828"))
        }

        return view
    }
}
