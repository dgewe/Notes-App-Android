package com.fredrikbogg.notesapp.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val KEY_SELECTED_THEME = "selected_theme"
    private const val PACKAGE_NAME = "com.fredrikbogg.notesapp"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getSelectedTheme(context: Context, fallbackTheme: Int): Int {
        return getPrefs(context).getInt(KEY_SELECTED_THEME, fallbackTheme)
    }

    fun saveSelectedTheme(context: Context, selectedThemeId: Int) {
        getPrefs(context).edit().putInt(KEY_SELECTED_THEME, selectedThemeId).apply()
    }
}