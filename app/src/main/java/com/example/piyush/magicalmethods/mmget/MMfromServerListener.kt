package com.example.piyush.magicalmethods.mmget

import com.beust.klaxon.JsonObject

/**
 * Created by ashish kumar on 04-11-2017.
 */
interface MMfromServerListener {
    fun onDownloadComplete(json: JsonObject)
}