package com.example.piyush.magicalmethods

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import kotlinx.android.synthetic.main.content_course_details_new.*


class CourseDetailsNew : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details_new)
//        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        val tests = ArrayList<Int>()
        for (i in 1..15)
            tests.add(1)
        val adapter = CourseDetailsVideosAdapter(this, tests)
        course_details_videos_recyclerview.adapter = adapter
        course_details_videos_recyclerview.layoutManager = LinearLayoutManager(this)
        course_details_videos_recyclerview.hasFixedSize()
        course_details_videos_recyclerview.isNestedScrollingEnabled = false

//        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_icon)
//        rotation.repeatCount = Animation.INFINITE
//        video_details_open_icon.startAnimation(rotation)

//        val matrix = Matrix()
//        video_details_open_icon.scaleType = ImageView.ScaleType.MATRIX   //required
//        matrix.postRotate(180F, (video_details_open_icon.drawable.bounds.width()/2).toFloat(), (video_details_open_icon.drawable.bounds.height()/2).toFloat())
//        video_details_open_icon.imageMatrix = matrix

        course_details_toggle_description.setOnClickListener { toggleVideoDetailsOpenIconRotation() }

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

    private fun toggleVideoDetailsOpenIconRotation()
    {
        if (course_details_open_icon.rotation == 90F)
            course_details_open_icon.rotation = 0F
        else
            course_details_open_icon.rotation = 90F

        if (course_details_description.visibility == View.GONE)
            course_details_description.visibility = View.VISIBLE
        else
            course_details_description.visibility = View.GONE
    }

}
