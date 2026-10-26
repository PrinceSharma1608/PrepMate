package com.PrepMate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookmark(question: BookmarkedQuestion)

    @Query("DELETE FROM bookmarked_questions WHERE qid = :qid")
    suspend fun removeBookmark(qid: String)

    @Query("SELECT * FROM bookmarked_questions ORDER BY qid")
    suspend fun getAllBookmarks(): List<BookmarkedQuestion>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_questions WHERE qid = :qid)")
    suspend fun isBookmarked(qid: String): Boolean

    // ---------- QUIZ ATTEMPT METHODS ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttempt)

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    suspend fun getAllAttempts(): List<QuizAttempt>

    @Query("SELECT * FROM quiz_attempts WHERE firestoreId = :id")
    suspend fun getAttemptById(id: String): QuizAttempt?
}