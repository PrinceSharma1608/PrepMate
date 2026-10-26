package com.PrepMate

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AttemptDetailActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_detail)

        listView = findViewById(R.id.listViewAttemptDetails)
        progressBar = findViewById(R.id.progressBarDetail)

        val firestoreId = intent.getStringExtra("firestoreId")

        if (firestoreId.isNullOrEmpty()) {
            progressBar.visibility = View.GONE
            return
        }

        fetchAttemptDetails(firestoreId)
    }

    private fun fetchAttemptDetails(firestoreId: String) {
        progressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance()
            .collection("Attemps")
            .document(firestoreId)
            .get()
            .addOnSuccessListener { document ->
                progressBar.visibility = View.GONE

                if (document != null && document.exists()) {
                    @Suppress("UNCHECKED_CAST")
                    val responses = document.get("responses") as? List<Map<String, Any>> ?: emptyList()

                    val adapter = AttemptDetailListAdapter(this, responses)
                    listView.adapter = adapter
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
            }
    }
}
