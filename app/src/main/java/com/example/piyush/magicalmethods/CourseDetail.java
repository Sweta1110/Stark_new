package com.example.piyush.magicalmethods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by USER
 */

import com.beust.klaxon.JsonObject;
import com.example.piyush.magicalmethods.databinding.ActivityCourseDetailBinding;
import com.example.piyush.magicalmethods.mmget.MMfromServer;
import com.example.piyush.magicalmethods.mmget.MMfromServerListener;

import org.jetbrains.annotations.NotNull;

public class CourseDetail extends AppCompatActivity {
    Button button;
    TextView tocHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        tocHeading=findViewById(R.id.tocHeading);
        button=findViewById(R.id.button);
        MyData myData = getIntent().getParcelableExtra("Card");
        ActivityCourseDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_course_detail);

        binding.setMyData(myData);
        binding.setPresenter(this);

        MMfromServer mMfromServer = new MMfromServer(this, new MMfromServerListener() {
            @Override
            public void onDownloadComplete(@NotNull JsonObject json) {
                System.out.println(json.toJsonString(true));
            }
        });
        mMfromServer.get(MMfromServer.Companion.getREQUEST_USER_COURSE_LIST(), "asaouetsfnvpsq");
        mMfromServer.execute();

        MMfromServer mMfromServerm = new MMfromServer(this, new MMfromServerListener() {
            @Override
            public void onDownloadComplete(@NotNull JsonObject json) {
                System.out.println(json.toJsonString(true));
            }
        });
        mMfromServerm.get(MMfromServer.Companion.getREQUEST_USER_COURSE_PURCHASED(), "asaouetsfnvpsq");
        mMfromServerm.execute();
    }

    private void openVideoPlayerActivity() {
        Intent intent = new Intent(CourseDetail.this, PaymentWebviewActivity.class);
        intent.putExtra("courseKey", "asaouetsfnvpsq");
        startActivity(intent);
    }

    public void onStartCourseClick(View view) {
        openVideoPlayerActivity();
    }
}
