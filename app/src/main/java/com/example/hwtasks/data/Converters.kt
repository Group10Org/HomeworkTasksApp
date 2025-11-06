package com.example.hwtasks.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate


class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter fun fromEpochDay(value: Long?): LocalDate? =
        value?.let { LocalDate.ofEpochDay(it) }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter fun localDateToEpochDay(date: LocalDate?): Long? =
        date?.toEpochDay()
}