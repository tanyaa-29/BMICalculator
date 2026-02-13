package com.example.bmicalculator.auth

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ─────────────────────────────────────────────────
// FILE: auth/AuthDatabase.kt
// Room database - stores users locally on device
// ─────────────────────────────────────────────────

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
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
                ).build().also { INSTANCE = it }
            }
        }
    }
}
