package com.example.piyush.magicalmethods.lib

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.piyush.magicalmethods.listeners.OnCreateSessionComplete
import com.github.kittinunf.fuel.httpPost

/**
* Created by ashish kumar
* on 24-12-2017 | 10:43 AM | 09:13 PM.
*/

class CreateSession(_context: Context, private val onCreateSessionComplete: OnCreateSessionComplete = OnCreateSessionComplete()) : AsyncTask<Void, Void, String>() {
    private val context = _context
    private val dhk = DHKeyExchange(context)
    private var modA = dhk.generateModA()
     val startTime = System.nanoTime()
     var endTime = 0L
     var elapsedTime = 0L


    override fun doInBackground(vararg p0: Void?): String {
        val args = listOf(
                "request" to "CREATE_SESSION",
                "version" to Util.getVersion(context),
                "time" to Util.getCurrentTimestamp(),
                "android_id" to Util.getAndroidId(),
                "country" to Util.getCountry(context),
                "state" to "na",
                "mod_a" to modA
        )

        val (_, response, result) = "http://mm.s-ct.asia/create.php".httpPost(args).responseString()
        val (_, error) = result

        return when (error) {
            null -> {
                String(response.data)
            }
            else -> {
                Log.d("CreateSession", "Error sending new session request")
                ""
            }
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        endTime = System.nanoTime()
        elapsedTime = endTime - startTime

        try {
            println(StringBuilder(result))
            // Json Parsing
            val parser = Parser()

            val resp = parser.parse(StringBuilder(result)) as JsonObject

            if (resp["result"] == "success") {
                val data = resp["data"] as JsonObject

                println(data["mod_b"])
                val sessionId = data["session_id"] as String
                val modB = data["mod_b"] as Int

                val key = dhk.generateKey(modB)

                val manageSession = ManageSession(context)
                manageSession.createSession(key, sessionId)

                onCreateSessionComplete.onCreate(this)
            }
        } catch (e: IllegalStateException) {
            Log.d("MMDownload", "Malformed json", e)
        } catch (e: Exception) {
            Log.d("MMDownload", "Malformed json", e)
        }
    }
}