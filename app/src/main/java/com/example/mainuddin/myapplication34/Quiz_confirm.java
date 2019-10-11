package com.example.mainuddin.myapplication34;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.ui.Quiz.quiz_page;
import com.example.mainuddin.myapplication34.ui.data.DatabaseHelper;
import com.example.mainuddin.myapplication34.ui.data.word;
import com.example.mainuddin.myapplication34.ui.data.word_details;
import com.example.mainuddin.myapplication34.ui.tools.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Quiz_confirm extends AppCompatActivity {

    List<word> contactList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_confirm);

        DatabaseHelper mDBHelper = new DatabaseHelper(this);


        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if(cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();
                word.setID( Integer.parseInt(cursor.getString(0)));
                word.setWORD( cursor.getString(1));
                word.setMEANING(cursor.getString(2));

                contactList.add(word);

            }


        }
        else {

            Toasty.error(Quiz_confirm.this,"Error Nothing found", Toast.LENGTH_LONG).show();
        }



        Button button = findViewById(R.id.strtquiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactList.size()>=4){
                    //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                   // MainActivity.score = prefs.getInt("highscore", 0);
                    Intent myIntent = new Intent(Quiz_confirm.this, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 1);}
                else{
                    Toasty.error(Quiz_confirm.this,"Sorry Collect more then 4 words.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
