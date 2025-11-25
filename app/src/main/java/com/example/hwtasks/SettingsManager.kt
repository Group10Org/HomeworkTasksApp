package com.example.hwtasks

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("task_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SORT_BY_DUE_DATE = "sort_by_due_date"
        private const val KEY_SORT_BY_PRIORITY = "sort_by_priority"
    }

    var sortByDueDate: Boolean
        get() = prefs.getBoolean(KEY_SORT_BY_DUE_DATE, true) // default to due date
        set(value) = prefs.edit().putBoolean(KEY_SORT_BY_DUE_DATE, value).apply()

    var sortByPriority: Boolean
        get() = prefs.getBoolean(KEY_SORT_BY_PRIORITY, false)
        set(value) = prefs.edit().putBoolean(KEY_SORT_BY_PRIORITY, value).apply()
}