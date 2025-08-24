package com.countjoy.data.local.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DateTimeConverter {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
        }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
    
    @TypeConverter
    fun fromInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }
    
    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
    
    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }
    
    @TypeConverter
    fun localDateToLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}