package com.aldroid.muzikashqipx.utils

import android.preference.PreferenceManager
import com.aldroid.opencamera.MainApp

class PrefManager {


    val preferences =  PreferenceManager.getDefaultSharedPreferences(MainApp.instance)

    fun getAccessToken(): String? {
        return preferences.getString(ACCESS_TOKEN, null)
    }

    fun setAccessToken(accessToken : String?) {
        preferences.edit().putString(ACCESS_TOKEN, accessToken).commit()
    }

    fun getUserId(): String? {
        return preferences.getString(USER_ID, null)
    }

    fun setUserId(userId : String) {
        preferences.edit().putString(USER_ID, userId).commit()
    }

    fun logout(){
        preferences.edit().putString(USER_ID, null).commit()
        preferences.edit().putString(ACCESS_TOKEN, null).commit()
    }

    val USER_ID = "user-id"
    val ACCESS_TOKEN = "access-token"

}