package com.example.piyush.magicalmethods

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by ashish kumar
 * on 08-02-2018 | 02:32 AM.
 */

class CourseDetailsVideosAdapter(private val context: Context, private val tests: ArrayList<Int>) : RecyclerView.Adapter<CourseDetailsVideosAdapter.MyViewAdapter>() {


    override fun onBindViewHolder(holder: MyViewAdapter?, position: Int) {
//        if (tests[position] == 1)
//            1

    }

    override fun getItemCount(): Int {
        return tests.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewAdapter {
        val inflater = LayoutInflater.from(parent?.context)
        val v: View = inflater.inflate(R.layout.video_course_details_item, parent, false)


        // Set view's sizes, margins, paddings and layout parameters
        // Returns as a viewholder
        return this.MyViewAdapter(v)
    }

    inner class MyViewAdapter(v: View) : RecyclerView.ViewHolder(v) {
        var layout: View = v
    }
}