package com.PrepMate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.PrepMate.data.local.QuizAttempt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttemptAnalysisAdapter(
    private val attempts: List<QuizAttempt>
) : RecyclerView.Adapter<AttemptAnalysisAdapter.AttemptViewHolder>() {

    class AttemptViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subjectText: TextView = view.findViewById(R.id.attemptSubjectText)
        val scoreText: TextView = view.findViewById(R.id.attemptScoreText)
        val timestampText: TextView = view.findViewById(R.id.attemptTimestampText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttemptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attempt_card, parent, false)
        return AttemptViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttemptViewHolder, position: Int) {
        val attempt = attempts[position]

        holder.subjectText.text = attempt.subject
        holder.scoreText.text = "Score: ${attempt.score}/${attempt.totalQuestions}"

        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = sdf.format(Date(attempt.timestamp))
        holder.timestampText.text = "Attempted on: $dateString"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AttemptDetailActivity::class.java).apply {
                putExtra("firestoreId", attempt.firestoreId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = attempts.size
}
