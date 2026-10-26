
package com.PrepMate

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.PrepMate.data.local.PrepMateDatabase
import kotlinx.coroutines.launch

class BookmarksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: BookmarkAdapter

    private val bookmarks =
        mutableListOf<com.PrepMate.data.local.BookmarkedQuestion>()

    private val dao by lazy {
        PrepMateDatabase.getDatabase(applicationContext)
            .bookmarkDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        recyclerView = findViewById(R.id.rvBookmarks)
        emptyText = findViewById(R.id.tvEmptyBookmarks)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BookmarkAdapter(bookmarks) { bookmark ->
            lifecycleScope.launch {
                dao.removeBookmark(bookmark.qid)

                adapter.removeItem(bookmark)
                updateEmptyState()
            }
        }

        recyclerView.adapter = adapter

        loadBookmarks()
    }

    private fun loadBookmarks() {
        lifecycleScope.launch {
            val savedBookmarks = dao.getAllBookmarks()

            bookmarks.clear()
            bookmarks.addAll(savedBookmarks)
            adapter.notifyDataSetChanged()

            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        val isEmpty = bookmarks.isEmpty()

        emptyText.visibility =
            if (isEmpty) View.VISIBLE else View.GONE

        recyclerView.visibility =
            if (isEmpty) View.GONE else View.VISIBLE
    }
}
