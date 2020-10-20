package com.example.notes;

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


public class MainActivity extends AppCompatActivity {

    ImageView imageView, red, green, red2, green2;
    TextView textView;
    Animation top, bottom;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);
        String theme = sharedPreferences.getString("MODE", "LIGHT");
        if(theme.equals("NIGHT")) {
            Log.i("TAG", "CHANGE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            Log.i("TAG", "CHANGE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
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
        },2000);
    }

}