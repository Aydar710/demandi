package com.aydar.demandi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aydar.demandi.data.DAO.QuestionDao
import com.aydar.demandi.data.DAO.RoomDao
import com.aydar.demandi.data.model.QuestionCache

@Database(entities = [QuestionCache::class, com.aydar.demandi.data.model.Room::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "demandi.db"
        )
            .build()
    }
}