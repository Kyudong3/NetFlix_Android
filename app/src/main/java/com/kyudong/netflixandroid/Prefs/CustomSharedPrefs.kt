package com.kyudong.netflixandroid.Prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Kyudong on 04/02/2019.
 */
class CustomSharedPrefs(context: Context) {

    private val NAME = "Netflix_prefs"
    private val MODE = Context.MODE_PRIVATE
    private val USER_TOKEN = "User_token"
    private val USER_EMAIL = "User_email"
    private val USER_ID = "User_id"
    private val USER_NICKNAME = "User_nickname"
    private val POST_REFRESH = "Refresh"
    private val prefs: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    /** 토큰 **/
    var userToken: String
        get() = prefs.getString(USER_TOKEN, "")
        set(value) = prefs.edit().putString(USER_TOKEN, value).apply()

    /** 이메일 **/
    var userEmail: String
        get() = prefs.getString(USER_EMAIL, "")
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()

    /** I D **/
    var userId: String
        get() = prefs.getString(USER_ID, "")
        set(value) = prefs.edit().putString(USER_ID, value).apply()

    /** 닉네임 **/
    var userNickname: String
        get() = prefs.getString(USER_NICKNAME, "")
        set(value) = prefs.edit().putString(USER_NICKNAME, value).apply()

    /** **/
    var mRefresh : String
        get() = prefs.getString(POST_REFRESH, "")
        set(value) = prefs.edit().putString(POST_REFRESH, value).apply()
}