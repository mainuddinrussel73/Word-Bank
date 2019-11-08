package com.example.czgame.wordbank.ui.words;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class add_page extends AppCompatActivity {


    TextInputEditText word;
    TextInputLayout w;
    TextInputEditText meaning;
    TextInputLayout m;
    Button done;
    private DatabaseHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        word = findViewById(R.id.word1);
        w = findViewById(R.id.wordlay);
        meaning = findViewById(R.id.meaning1);
        m = findViewById(R.id.meaninglay);

        mDBHelper = new DatabaseHelper(this);
        done = findViewById(R.id.done);
        final RelativeLayout additem = findViewById(R.id.add_item);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {
            additem.setBackgroundColor(Color.BLACK);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            word.setHintTextColor(Color.rgb(185, 185, 185));
            meaning.setHintTextColor(Color.rgb(185, 185, 185));
            word.setTextColor(Color.WHITE);
            meaning.setTextColor(Color.WHITE);
            w.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_page.this, R.color.divider)));
            m.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_page.this, R.color.divider)));
        } else {
            additem.setBackgroundColor(Color.WHITE);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            word.setHintTextColor(Color.BLACK);
            meaning.setHintTextColor(Color.BLACK);
            setUpperHintColor(Color.BLACK);
            setUpperHintColor1(Color.BLACK);
            w.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_page.this, R.color.darkgray)));
            m.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_page.this, R.color.darkgray)));
            word.setTextColor(Color.BLACK);
            meaning.setTextColor(Color.BLACK);
        }


        word.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (!arg1) {
                    DatabaseHelper mDBHelper = new DatabaseHelper(add_page.this);
                    // SQLiteDatabase mDb;
                    String id = "-1";
                    //SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
                    try {
                        Cursor re = db1.rawQuery("SELECT * FROM Word_table WHERE WORD = ?; ", new String[]{word.getText().toString()});
                        if (re.moveToFirst()) {
                            do {
                                System.out.println(re.getString(0));
                                id = re.getString(0);
                            } while (re.moveToNext());
                        }

                        re.close();
                        // System.out.println(re.getString(0));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (!id.equals("-1")) {
                        Toasty.warning(add_page.this, "Data already exist", Toasty.LENGTH_LONG).show();
                        done.setEnabled(false);
                    } else {
                        done.setEnabled(true);
                    }
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                if (word.getText().toString().isEmpty() || word.getText().toString().trim().length() <= 0) {
                    Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                } else if (meaning.getText().toString().isEmpty() || meaning.getText().toString().trim().length() <= 0) {
                    Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean b = mDBHelper.insertData(word.getText().toString(), meaning.getText().toString(), "");
                    if (b == true) {
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    } else {
                        Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
    private void setUpperHintColor(int color) {
        try {
            Field field = w.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(w, myList);

            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(w, myList);

            Method method = w.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(w, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setUpperHintColor1(int color) {
        try {
            Field field = m.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(m, myList);

            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(m, myList);

            Method method = m.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(m, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //m.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, color)));
    }
}

