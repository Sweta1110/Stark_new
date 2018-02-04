package com.example.piyush.magicalmethods.lib

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by ashish kumar on 28-12-2017 | 08:00 PM.
 */
class ManageSession(private val context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Saves the key and session generated from the session to
     * shared preferences
     */
    fun createSession(key: String, sessionId: String) {
        val preferencesEdit = preferences.edit()

        preferencesEdit.putString("key", key)
        preferencesEdit.putString("session_id", sessionId)

        preferencesEdit.apply()
    }

    fun isSessionValid(): Boolean {
        val key = preferences.getString("key", "")
        val sessionId = preferences.getString("session_id", "")
        println(key + " " + sessionId)
        return key != "" && sessionId != ""
    }

    /**
     * Returns key and session id from shared preferences
     * @return Pair<String, String>: <Key, SessionId>
     */
    fun getSession(): Pair<String, String> {
        val key = preferences.getString("key", "")
        val sessionId = preferences.getString("session_id", "")
        return Pair(key, sessionId)
    }
}