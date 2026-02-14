package com.example.bmicalculator.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET passwordHash = :newHash WHERE email = :email")
    suspend fun updatePassword(email: String, newHash: String)

    // Weight History
    @Insert
    suspend fun insertWeight(weight: WeightHistory)

    @Query("SELECT * FROM weight_history WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getWeightHistory(userId: Int): List<WeightHistory>

    @Query("SELECT * FROM weight_history WHERE userId = :userId AND timestamp >= :since ORDER BY timestamp ASC")
    suspend fun getRecentWeightHistory(userId: Int, since: Long): List<WeightHistory>

    // Multi-user support: Get all profiles
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
}
