package com.example.bmicalculator.auth

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,          // used as username for login
    val passwordHash: String,   // MD5 hash of the password
    val secretQuestion: String, // for forgot password
    val secretAnswer: String,   // hashed answer
    val createdAt: Long = System.currentTimeMillis()
)
