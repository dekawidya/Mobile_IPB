package com.example.lab_00.appku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("statusLogin", MODE_PRIVATE);

                Intent i;

                if (sp.getString("username", null) == null) {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }

                startActivity(i);
            }
        }, 3000);
    }
}
