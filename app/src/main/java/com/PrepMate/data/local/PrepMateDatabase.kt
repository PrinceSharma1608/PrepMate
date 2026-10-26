package com.PrepMate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookmarkedQuestion::class,
        QuizAttempt::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PrepMateDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    companion object {

        @Volatile
        private var INSTANCE: PrepMateDatabase? = null

        fun getDatabase(context: Context): PrepMateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrepMateDatabase::class.java,
                    "prepmate_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
