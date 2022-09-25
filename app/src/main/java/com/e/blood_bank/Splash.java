package com.e.blood_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

class SplashTimer extends Thread
{
    Splash splash;

    SplashTimer( Splash splash)
    {
        this.splash= splash;

    }
    public void run()
    {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{

            Intent iobj = new Intent(splash,WelcomeScreen.class);
            splash.startActivity(iobj);

            splash.finish();


        }


    }
}

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashTimer timer= new SplashTimer(Splash.this);
        timer.start();


    }
}

