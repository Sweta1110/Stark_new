package com.example.piyush.magicalmethods.mmget


import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.piyush.magicalmethods.lib.ManageSession
import com.example.piyush.magicalmethods.lib.Util
import com.github.kittinunf.fuel.httpPost
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by ashish kumar on 04-11-2017 | 04:05 PM.
 */
class MMfromServer(_context: Context, _listener: MMfromServerListener) : AsyncTask<Void, Void, String>() {
    private val context = _context
    private val listener = _listener
    private var mcryptKey = ""

    private var request = 0
    private var courseKey = ""

    private val baseUrl = "http://mm.s-ct.asia/"

    companion object {
        val REQUEST_VIDEO_LIST = 1
        val REQUEST_COURSE_LIST = 2
        val REQUEST_COURSE_INFO = 3
        val REQUEST_USER_COURSE_LIST = 21
        val REQUEST_USER_COURSE_PURCHASED = 22
    }

    /**
     * It sets the type of request and relevant data to be sent
     * to server. It doesn't do anything just sets the variable
     * which is used when AsyncTask is executed, i.e., doInBackground
     * method is executed
     */
    fun get(type: Int, _course_key: String = "") {
        request = type
        courseKey = _course_key
    }


    override fun doInBackground(vararg p0: Void?): String {
//        mcryptKey = generateKey()
        val session = ManageSession(context).getSession()
        mcryptKey = session.first
        val sessionId = session.second
        println(sessionId)


        val args = mutableListOf("session_id" to sessionId, "version" to Util.getVersion(context))

        when (request) {
            REQUEST_COURSE_LIST -> args.add("request" to "COURSE_LIST")
            REQUEST_COURSE_INFO -> {
                args.add("request" to "COURSE_INFO")
                if (courseKey != "")
                    args.add("course_key" to courseKey)
                else
                    throw Exception("Not a valid course key")
            }
            REQUEST_VIDEO_LIST -> {
                args.add("request" to "VIDEO_LIST")
                if (courseKey != "")
                    args.add("course_key" to courseKey)
                else
                    throw Exception("Not a valid course key")
            }
            REQUEST_USER_COURSE_LIST -> {
                args.add("request" to "COURSE_LIST")
                args.add("user_id" to getUser())
            }
            REQUEST_USER_COURSE_PURCHASED -> {
                args.add("request" to "COURSE_PURCHASED")
                args.add("user_id" to getUser())
                if (courseKey != "")
                    args.add("course_key" to courseKey)
                else
                    throw Exception("Not a valid course key")
            }
            else -> {
                throw Exception("Not a valid request type")
            }
        }

        val (_, response, result) = getUrl().httpPost(args).responseString()
        val (_, error) = result

        return when (error) {
            null -> {
                //            Log.d("mmtest", response.toString())
                println(String(response.data))
                String(response.data)
            }
            else -> {
                Log.d("mmtest", "error")
                ""
            }
        }
    }

    private fun decodeResponse(result: String?): JsonObject? {
        try {
            // Decode the response from the server using decrypt
            val mcrypt = mcrypt(mcryptKey)
            val resp = String(mcrypt.decrypt(result))

            // Json Parsing
            val parser = Parser()

            println(resp)
            return parser.parse(StringBuilder(resp)) as JsonObject
        } catch (e: IllegalStateException) {
            Log.d("MMDownload", "Malformed json", e)
        } catch (e: Exception) {
            Log.d("MMDownload", "Malformed json", e)
        }

        return JsonObject()
    }

    private fun getUrl(): String {
        return when {
            request < REQUEST_USER_COURSE_LIST -> baseUrl + "get.php"
            request <= REQUEST_USER_COURSE_PURCHASED -> baseUrl + "user.php"
            else -> baseUrl
        }
    }

    private fun getUser(): String {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        return user?.uid ?: "default"
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if (!isCancelled && result != "") {
            val decoded = decodeResponse(result)
            listener.onDownloadComplete(decoded!!)
        }
    }
}