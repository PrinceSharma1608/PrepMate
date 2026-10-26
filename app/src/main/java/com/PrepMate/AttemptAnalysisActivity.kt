package com.PrepMate

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.PrepMate.data.local.PrepMateDatabase
import com.PrepMate.data.local.QuizAttempt
import kotlinx.coroutines.launch

class AttemptAnalysisActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: AttemptAnalysisAdapter

    private val attempts = mutableListOf<QuizAttempt>()

    private val dao by lazy {
        PrepMateDatabase.getDatabase(applicationContext)
            .bookmarkDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_analysis)

        recyclerView = findViewById(R.id.rvAttempts)
        emptyText = findViewById(R.id.tvEmptyAttempts)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AttemptAnalysisAdapter(attempts)
        recyclerView.adapter = adapter

        loadAttempts()
    }

    private fun loadAttempts() {
        lifecycleScope.launch {
            val savedAttempts = dao.getAllAttempts()

            attempts.clear()
            attempts.addAll(savedAttempts)
            adapter.notifyDataSetChanged()

            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        val isEmpty = attempts.isEmpty()

        emptyText.visibility =
            if (isEmpty) View.VISIBLE else View.GONE

        recyclerView.visibility =
            if (isEmpty) View.GONE else View.VISIBLE
    }
}
