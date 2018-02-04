package com.example.piyush.magicalmethods;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piyush.magicalmethods.lib.CreateSession;
import com.example.piyush.magicalmethods.lib.ManageSession;
import com.example.piyush.magicalmethods.listeners.OnCreateSessionComplete;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    /**
     * Created by SWE
     */
    private boolean sessionCreated = false;
    private final Context context = this;


    private final int SPLASH_DISPLAY_LENGTH = 2000;

    ImageView i1;

    @Override
    public void onCreate(Bundle icicle) {
        try {
            super.onCreate(icicle);
            setContentView(R.layout.activity_splash_screen);


            ManageSession manageSession = new ManageSession(context);
            if (!manageSession.isSessionValid()) {
                final CreateSession createSession = new CreateSession(this, new OnCreateSessionComplete() {
                    @Override
                    public void onCreate(CreateSession createSession1) {
                        Double elapsedTime = createSession1.getElapsedTime() / 1.0e6;
                        sessionCreated = true;
                        Toast.makeText(context, "Session Created", Toast.LENGTH_SHORT).show();
                        if (elapsedTime > SPLASH_DISPLAY_LENGTH) {
                            Toast.makeText(context, "Opening Login activity", Toast.LENGTH_SHORT).show();
                            openLoginActivity();
                        }
                    }
                });
                createSession.execute();
            } else
                sessionCreated = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sessionCreated)
                        openLoginActivity();
                    else
                        Toast.makeText(context, "Creating Session", Toast.LENGTH_LONG).show();
                }
            }, SPLASH_DISPLAY_LENGTH);
            i1 = (ImageView) findViewById(R.id.iv);

        } catch (Exception e) {
            Log.e(TAG, "OnCreate_in_SplashScreen_class", e);
            throw e;
        }
    }

    private void openLoginActivity() {
        Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();
    }
}



