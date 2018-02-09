package com.example.piyush.magicalmethods


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_payment_response.*
import kotlinx.android.synthetic.main.content_payment_response.*

class PaymentResponseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_response)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        open_video_player.setOnClickListener {
            startActivity(Intent(this, VideoPlayerActivity::class.java))
        }
    }

}
