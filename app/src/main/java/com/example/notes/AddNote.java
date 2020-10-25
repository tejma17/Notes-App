package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.transition.Explode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    private EditText note;
    private Button done, back;
    private int noteId;
    private ArrayList<Notes> notes;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementExitTransition(new Explode());

        setContentView(R.layout.activity_add_note);

        sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);
        String connectionsJSONString = sharedPreferences.getString("Name", null);
        Type type = new TypeToken< ArrayList < Notes >>() {}.getType();
        notes = new Gson().fromJson(connectionsJSONString, type);

        note = findViewById(R.id.newNote);
        done = findViewById(R.id.button);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        if(noteId != -1){
            note.setText(notes.get(noteId).getName());
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, note.getText().toString().trim());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String formattedDate = df.format(c);

        if(note.getText().toString().trim().equals("")){
            if(noteId != -1)
                notes.remove(noteId);
        }else {
                if (noteId != -1) {
                    if(!note.getText().toString().trim().equals(notes.get(noteId).getName()))
                        notes.set(noteId, new Notes(note.getText().toString().trim(), formattedDate));
                } else {
                    notes.add(new Notes(note.getText().toString().trim(), formattedDate));
                }
        }
        String connectionsJSONString = new Gson().toJson(notes);
        sharedPreferences.edit().putString("Name", connectionsJSONString).apply();
        super.onBackPressed();
    }
}