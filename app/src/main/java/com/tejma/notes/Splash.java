package com.tejma.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notes.R;


public class Splash extends AppCompatActivity {

    private ImageView imageView, red, green, red2, green2;
    private TextView textView;
    private Animation top, bottom;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);
        String theme = sharedPreferences.getString("MODE", "System Default");
        if(theme.equals("Dark")) {
            Log.i("TAG", "CHANGE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(theme.equals("Light")){
            Log.i("TAG", "CHANGE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        textView = findViewById(R.id.appname);
        imageView = findViewById(R.id.logo);
        red = findViewById(R.id.redline);
        green = findViewById(R.id.greenline);
        green2 = findViewById(R.id.greenbottom);
        red2 = findViewById(R.id.redbottom);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bot_animation);

        imageView.animate().alpha(1).setDuration(1000);
        red.setAnimation(top);
        green.setAnimation(top);
        red2.setAnimation(bottom);
        green2.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        },1500);
    }

}