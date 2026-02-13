package com.example.bmicalculator.auth

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weight_history",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class WeightHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val weightKg: Float,
    val timestamp: Long = System.currentTimeMillis()
)
