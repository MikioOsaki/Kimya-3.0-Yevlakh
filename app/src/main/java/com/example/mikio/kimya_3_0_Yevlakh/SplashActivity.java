package com.example.mikio.kimya_3_0_Yevlakh;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(2700);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                    Intent i=new Intent(SplashActivity.this,MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();
    }
}