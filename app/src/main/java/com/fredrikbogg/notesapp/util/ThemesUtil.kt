package com.fredrikbogg.notesapp.util

import android.app.Activity
import android.content.Context
import com.fredrikbogg.notesapp.R


object ThemesUtil {

    fun changeTheme(themeId: Int, activity: Activity) {
        SharedPreferencesUtil.saveSelectedTheme(activity, themeId)
        activity.recreate()
    }

    fun getCurrentTheme(context: Context): Int {
        return SharedPreferencesUtil.getSelectedTheme(context, R.style.DefaultTheme)
    }
}