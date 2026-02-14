package com.example.bmicalculator.auth

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, WeightHistory::class], version = 2, exportSchema = false)
abstract class AuthDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AuthDatabase? = null

        fun getInstance(context: Context): AuthDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AuthDatabase::class.java,
                    "bmi_auth.db"
                )
                .fallbackToDestructiveMigration() // Simplified for development; clears data on schema change
                .build().also { INSTANCE = it }
            }
        }
    }
}
