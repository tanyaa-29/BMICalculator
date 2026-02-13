package com.example.bmicalculator.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :hash LIMIT 1")
    suspend fun login(email: String, hash: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int

    @Query("UPDATE users SET passwordHash = :newHash WHERE email = :email")
    suspend fun updatePassword(email: String, newHash: String)
}
