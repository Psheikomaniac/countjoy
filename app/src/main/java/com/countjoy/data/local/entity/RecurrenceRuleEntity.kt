package com.countjoy.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "recurrence_rules",
    foreignKeys = [
        ForeignKey(
            entity = CountdownEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class RecurrenceRuleEntity(
    @PrimaryKey
    val id: String,
    val eventId: String,
    val pattern: String, // RecurrencePattern enum as string
    val intervalValue: Int,
    val daysOfWeek: String?, // Comma-separated day names
    val dayOfMonth: Int?,
    val weekOfMonth: Int?,
    val monthOfYear: Int?,
    val endType: String, // RecurrenceEndType enum as string
    val endDate: LocalDate?,
    val occurrenceCount: Int?,
    val exceptions: String?, // Comma-separated dates
    val skipWeekends: Boolean,
    val skipHolidays: Boolean,
    val lastOccurrenceDate: LocalDate?, // Track last generated occurrence
    val nextOccurrenceDate: LocalDate? // Pre-calculated next occurrence
)