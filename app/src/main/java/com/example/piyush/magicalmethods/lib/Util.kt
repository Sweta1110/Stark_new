package com.example.piyush.magicalmethods.lib

import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.provider.Settings
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * Created by ashish kumar on 28-12-2017 | 05:27 PM.
 */
class Util {
    companion object {
        fun md5(message: String): String {
            return try {
                // Creating md5 hash
                val digest = MessageDigest.getInstance("MD5")
                digest.update(message.toByteArray())
                val messageDigest = digest.digest()

                // Creating hex string from message digest
                val hexString = StringBuffer()
                for (i in 0..(messageDigest.size - 1)) {
                    val hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
                    if (hex.length == 1)
                        hexString.append("0")
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                ""
            }
        }

        /**
         * Returns version of the app installed
         * @return Int
         */
        fun getVersion(context: Context): Int {
            return try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                0
            }
        }

        /**
         * Time passed since epoch
         * in seconds
         */
        fun getCurrentTimestamp(): Long {
            val date = Calendar.getInstance().time;
            return date.time
        }

        /**
         * Returns android id
         * No more recommended by google
         * use instance id instead
         */
        fun getAndroidId(): String {
            return Settings.Secure.ANDROID_ID
        }

        fun getInstanceId(): String {
            // @TODO Replace Android id with instance id
            return ""
        }

        /**
         * Returns full country name of the user based on configuration
         * Saves the country in preference manager for further calling
         */
        fun getCountry(context: Context): String {
            var country = PreferenceManager.getDefaultSharedPreferences(context).getString("country", "DEFAULT")
            if (country != null)
                return country

            // @TODO use location manager, telephony manager, networks' providers for finding user country
            country = context.resources.configuration.locale.country
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("country", country).apply()
            // @TODO Create a class to handle preference manager
            return country
        }
  
        fun getState() {
            // @TODO Implement getstate
        }
    }
}