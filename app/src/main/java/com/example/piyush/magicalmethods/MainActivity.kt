package com.example.piyush.magicalmethods

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.example.piyush.magicalmethods.lib.CreateSession
import com.example.piyush.magicalmethods.lib.ManageSession
import com.example.piyush.magicalmethods.lib.Util
import com.example.piyush.magicalmethods.mmget.MMfromServer
import com.example.piyush.magicalmethods.mmget.MMfromServerListener

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("\n" + Util.md5("234342"))

        val manageSession = ManageSession(this)
        if (!manageSession.isSessionValid())
            CreateSession(this).execute()
        else {

            // Check logcat in Android studio for result
            val back = MMfromServer(this, createListener())
            back.get(MMfromServer.REQUEST_COURSE_INFO, "asaouetsfnvpsq")
            back.execute()

            val back2 = MMfromServer(this, createListener())
            back2.get(MMfromServer.REQUEST_VIDEO_LIST, "asaouetsfnvpsq")
            back2.execute()

            val back3 = MMfromServer(this, createListener())
            back3.get(MMfromServer.REQUEST_COURSE_LIST)
            back3.execute()
        }
    }


    /**
     * @return "MMfromServerListener" object that handles the data if successfully downloaded
     * It is just to show how to handle the data returned from the server
     * Not intended to be in final product
     *
     * Anything that will be in onDownloadComplete Function will execute in UI thread
     * i.e., you could change ui (add items in listview. etc.) from the function
     */
    private fun createListener(): MMfromServerListener {
        return object : MMfromServerListener {
            override fun onDownloadComplete(json: JsonObject) {
                // Checks if there wasn't any error like invalid session id, or other server related issut
                // It will be executed only when data is successfully recieved
                if (json["result"] == "success") {
                    Log.d("MMDownload", "success: " + json.toJsonString(true))

                    when {
                    // Returned data is of JsonArray type
                    // List of course
                        json["data"] is JsonArray<*> -> for (course in json["data"] as JsonArray<*>) {
                            course as JsonObject
                            println("Name: " + course["name"] + "\nDescription: " + course["description"])
                        }

                    // Returned item is of JsonObject type
                    // Course data
                        json["data"] is JsonObject -> {
                            val course = json["data"] as JsonObject
                            println("Name: " + course["name"] + "\nDescription: " + course["description"])
                        }

                    // If returned data is of unknown format
                        else -> println("No data returned")
                    }
                } else {
                    Log.d("MMDownload", "error")
                }
            }
        }
    }
}
