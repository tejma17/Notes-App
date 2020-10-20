package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    FloatingActionButton add;
    SharedPreferences sharedPreferences;
    ListView list;
    private MaterialButton toggle;
    EditText search;
    LinearLayout search_layout;
    TextView cancel;
    private boolean isNight;
    ListViewAdapter arrayAdapter;
    public static ArrayList<Notes> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);
        if(sharedPreferences.getString("MODE", "LIGHT").equals("NIGHT")){
            isNight = true;
        }

        initElements();
        arrayAdapter = new ListViewAdapter(getApplicationContext(), R.layout.layout, notes);
        list.setAdapter(arrayAdapter);

        if(!isNight){
            toggle.setIconResource(R.drawable.ic_round_bedtime_24);
        }else{
            toggle.setIconResource(R.drawable.ic_round_wb_sunny_24);
        }

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    cancel.setVisibility(View.VISIBLE);
                }
                else {
                    cancel.setVisibility(View.GONE);
                }
            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNight) {
                    toggle.setIconResource(R.drawable.ic_round_wb_sunny_24);
                    sharedPreferences.edit().putString("MODE", "NIGHT").apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    isNight = true;
                } else {
                    toggle.setIconResource(R.drawable.ic_round_bedtime_24);
                    sharedPreferences.edit().putString("MODE", "LIGHT").apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    isNight = false;
                }
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
                startActivity(new Intent(getApplicationContext(), AddNOte.class), options.toBundle());
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddNOte.class);
                Pair[] pairs = new Pair[1];
                LinearLayout tv = (LinearLayout) view;
                pairs[0] = new Pair<View, String>(tv, "note_open");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Home.this, pairs);
                intent.putExtra("noteId", position);
                startActivity(intent, options.toBundle());
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                final int position = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Delete");
                builder.setMessage("Delete this note permanently?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                String connectionsJSONString = new Gson().toJson(notes);
                                sharedPreferences.edit().putString("Name", connectionsJSONString).apply();
                                Toast.makeText(Home.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)) {
                    arrayAdapter.filter("");
                    list.clearTextFilter();
                }
                else {
                    arrayAdapter.filter(s.toString());
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
        toggle = findViewById(R.id.mode);
        search_layout = findViewById(R.id.search_arent);
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
        arrayAdapter = new ListViewAdapter(getApplicationContext(), R.layout.layout, notes);
        list.setAdapter(arrayAdapter);
        super.onResume();
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
}