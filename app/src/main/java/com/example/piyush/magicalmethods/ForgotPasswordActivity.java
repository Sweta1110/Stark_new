package com.example.piyush.magicalmethods;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by SWE
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputEmail = (EditText) findViewById(R.id.frg_email);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnBack = (Button) findViewById(R.id.btn_back);


        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                 inputEmail.setText("");
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailAddress = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                     Toast.makeText(ForgotPasswordActivity.this, "We have sent email to reset your password!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                   
                       }
                                else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
           
            }
        });
    }
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
        /*if(newConfig.orientation== Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getApplicationContext(),"You are in Portrait mode",Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(getApplicationContext(),"You are in Landscape mode",Toast.LENGTH_SHORT).show();
        }*/
    }

}
