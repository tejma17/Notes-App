package com.tejma.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Home extends AppCompatActivity implements RecyclerViewAdapter.onNoteListener {

    private FloatingActionButton add;
    private SharedPreferences sharedPreferences;
    private RecyclerView list;
    private MaterialButton settings;
    private EditText search;
    private TextView cancel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Notes> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);

        initElements();
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), R.layout.recycler_parent, notes, this);
        list.setAdapter(recyclerViewAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        list.setLayoutManager(staggeredGridLayoutManager);
        
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    cancel.setVisibility(View.VISIBLE);
                    add.setVisibility(View.GONE);
                }
                else {
                    cancel.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(settings, "settings");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Home.this, pairs);
                startActivity(new Intent(getApplicationContext(), Settings.class), options.toBundle());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setVisibility(View.GONE);
                search.setFocusableInTouchMode(false);
                search.setFocusable(false);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(add, "add_button");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Home.this, pairs);
                startActivity(new Intent(getApplicationContext(), AddNote.class), options.toBundle());
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)) {
                    recyclerViewAdapter.filter("");
                  //  list.clearTextFilter();
                }
                else {
                    recyclerViewAdapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initElements() {
        add = findViewById(R.id.add);
        list = findViewById(R.id.list);
        settings = findViewById(R.id.mode);
        search = findViewById(R.id.search);
        cancel = findViewById(R.id.cancel);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                ((EditText) v).getText().clear();
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void onResume() {
        getData();
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), R.layout.recycler_parent, notes, this);
        list.setAdapter(recyclerViewAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        list.setLayoutManager(staggeredGridLayoutManager);
        super.onResume();
    }

    private void getData() {
        String connectionsJSONString = sharedPreferences.getString("Name", null);
        Type type = new TypeToken< ArrayList < Notes >>() {}.getType();
        ArrayList<Notes> notes_temp = new Gson().fromJson(connectionsJSONString, type);
        if(notes_temp == null){
            notes = new ArrayList<>();
            notes.add(new Notes("Example Note", "--"));
            String notes_list = new Gson().toJson(notes);
            sharedPreferences.edit().putString("Name", notes_list).apply();
        }else if(notes_temp.size() == 0){
            notes = new ArrayList<>();
            notes.add(new Notes("Example Note", "--"));
            String notes_list = new Gson().toJson(notes);
            sharedPreferences.edit().putString("Name", notes_list).apply();
        }else{
            notes = notes_temp;
        }
        Collections.sort(notes, new SortByDate());
    }

    @Override
    public void onBackPressed() {
        if(cancel.getVisibility() == View.VISIBLE) {
            cancel.setVisibility(View.GONE);
            search.setFocusableInTouchMode(false);
            search.setFocusable(false);
        }
        else
        finish();
    }

    @Override
    public void onNoteClick(int position, View view) {
        Intent intent = new Intent(getApplicationContext(), AddNote.class);
        Pair[] pairs = new Pair[1];
        LinearLayout tv = (LinearLayout) view;
        pairs[0] = new Pair<View, String>(tv, "note_open");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Home.this, pairs);
        intent.putExtra("noteId", position);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onNoteLongClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Delete");
        builder.setMessage("Delete this note permanently?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        recyclerViewAdapter.notifyDataSetChanged();
                        String connectionsJSONString = new Gson().toJson(notes);
                        sharedPreferences.edit().putString("Name", connectionsJSONString).apply();
                        Toast.makeText(Home.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    class SortByDate implements Comparator<Notes>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Notes a, Notes b)
        {
            return b.getDescription().compareTo(a.getDescription());
        }
    }
}