package com.tejma.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.notes.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements RecyclerViewAdapter.onNoteListener {

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Notes> demo;
    private AlertDialog.Builder builder;
    private MaterialButton back;
    private SharedPreferences sharedPreferences;
    private String theme, font, privacy;
    private String[] values, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RecyclerView listView = findViewById(R.id.settingView);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.super.onBackPressed();
            }
        });

        sharedPreferences = getSharedPreferences("Notes", MODE_PRIVATE);

        demo = new ArrayList<>();
        demo.clear();
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), R.layout.settings_layout, demo, this);
        listView.setAdapter(recyclerViewAdapter);
        listView.addItemDecoration(new DividerItemDecoration(listView.getContext(), DividerItemDecoration.VERTICAL));
        listView.setLayoutManager(new LinearLayoutManager(this));
        getStrings();
    }

    private void getStrings(){
        theme = sharedPreferences.getString("MODE", "Light");
        font = sharedPreferences.getString("FONT", "Small");
        privacy = "";
        values = new String[]{theme, font, privacy};
        settings = new String[]{
                "Theme",
                "Font size",
                "Privacy Policy"
        };
        for(int i=0; i<settings.length; i++){
            Notes chapters = new Notes(settings[i], values[i]);
            demo.add(chapters);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNoteClick(int position, View v) {
        final String[] font_size = {"Small", "Medium", "Large"};
        final String[] mode = {"Light", "Dark", "System Default"};
        if(position ==  0)
        {
            builder = new AlertDialog.Builder(this);
            builder.setItems(mode, (dialog, which) -> {
                if(which == 1)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else if(which == 0)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                else if(which == 2)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                theme = mode[which];
                sharedPreferences.edit().putString("MODE", mode[which]).apply();
                demo.get(position).setDescription(theme);
                recyclerViewAdapter.notifyDataSetChanged();
            }).show();
        }
        if(position == 1)
        {
            builder = new AlertDialog.Builder(this);
            builder.setItems(font_size, (dialog, which) -> {
                font = font_size[which];
                sharedPreferences.edit().putString("FONT", font_size[which]).apply();
                demo.get(position).setDescription(font);
                recyclerViewAdapter.notifyDataSetChanged();
            }).show();
        }
        if(position == 2){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://notes-7.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onNoteLongClick(int position) {

    }
}