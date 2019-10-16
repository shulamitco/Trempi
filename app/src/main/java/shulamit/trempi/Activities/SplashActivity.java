package shulamit.trempi.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import shulamit.trempi.R;

public class SplashActivity extends AppCompatActivity {
    private static final int DELAY_TIME = 2; // seconds
    private int timeLeft;
    private boolean isLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isLogIn = isLogIn();
        startTimer();
    }

    private boolean isLogIn() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.USER_DETAILS), MODE_PRIVATE);
        String name   = sharedPreferences.getString(getResources().getString(R.string.USER_NAME), "");
        String myToken   = sharedPreferences.getString(getResources().getString(R.string.USER_NUMBER), "");
        if(name == "" || myToken == "")
            return false;
        return true;
    }

    private void startTimer()
    {
        timeLeft = DELAY_TIME;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(timeLeft>=0){
                    SystemClock.sleep(1000); //Thread.sleep(1000);
                    timeLeft--;
                }
                // decide here whether to navigate to Login or Main Activity
                Intent intent = new Intent(SplashActivity.this, isLogIn == false ? LoginActivity.class: MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
