package com.example.czgame.wordbank.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class UpdateDiary  extends AppCompatActivity {
    TextInputEditText subjectEt;
    TextInputLayout s;
    TextInputEditText descriptionEt;
    TextInputLayout d;
    Button cancelBt,updateBt,shareBtOnUpdate;
    SqliteDatabase dbUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.update_diary);

        //passing Update Activity's context to database alass
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate1);
        s = findViewById(R.id.subjectEditTextIdUpdate);
        descriptionEt = findViewById(R.id.descriptionEditTextIdUpdate1);
        d = findViewById(R.id.descriptionEditTextIdUpdate);


        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);
        shareBtOnUpdate = findViewById(R.id.shareButtonIdUpdate);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);
        descriptionEt.setText(des);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.dialogId);
        RelativeLayout lidtb = findViewById(R.id.two);

        if (isDark) {

            laybase.setBackgroundColor(Color.BLACK);
            subjectEt.setHintTextColor(Color.rgb(185, 185, 185));
            subjectEt.setTextColor(Color.WHITE);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            descriptionEt.setHintTextColor(Color.rgb(185, 185, 185));
            descriptionEt.setTextColor(Color.WHITE);

            d.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark) {

            laybase.setBackgroundColor(Color.WHITE);
            subjectEt.setHintTextColor(Color.BLACK);
            subjectEt.setTextColor(Color.BLACK);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            descriptionEt.setHintTextColor(Color.BLACK);
            descriptionEt.setTextColor(Color.BLACK);

            d.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));


            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));

        }

        //for sharing data to social media
        shareBtOnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sub = subjectEt.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                String des = descriptionEt.getText().toString();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,des);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for updating database data
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy  hh:mm: a",date);

                if(dbUpdate.update(subjectEt.getText().toString(),descriptionEt.getText().toString(),"",d,id,0)==true){
                    Toasty.success(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
        });
    }

    //this method to clearing top activity and starting new activity
    public void backToMain()
    {
        Intent intent = new Intent(UpdateDiary.this,DiaryMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
