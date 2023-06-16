package com.tger.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity
{
    private static final int SPLASH_DURATION = 3000; // Splash screen duration in milliseconds
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //this hides the action bar in the splash screen activity
        getSupportActionBar().hide();

        // Delay opening the next activity using a Handler
        new Handler().postDelayed(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Start the next activity
                        Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the splash activity
                    }
                }, SPLASH_DURATION);

    }
}