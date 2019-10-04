package com.example.mainuddin.myapplication34.ui.insert;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class add_page extends AppCompatActivity {



    private DatabaseHelper mDBHelper;
    EditText word ;
    EditText meaning;
    Button done;

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

        word =  (EditText) findViewById(R.id.word1);
        meaning = (EditText) findViewById(R.id.meaning1);

        mDBHelper = new DatabaseHelper(this);
        done = (Button) findViewById(R.id.done);
        LinearLayout additem = findViewById(R.id.add_item);

        if(MainActivity.isDark){
            additem.setBackgroundColor(Color.rgb(64,64,64));
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            word.setHintTextColor(Color.rgb(185,185,185));
            meaning.setHintTextColor(Color.rgb(185,185,185));
        }else{
            additem.setBackgroundColor(Color.WHITE);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            word.setHintTextColor(Color.rgb(64,64,64));
            meaning.setHintTextColor(Color.rgb(64,64,64));
        }



        done.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                boolean b = mDBHelper.insertData(word.getText().toString(),meaning.getText().toString(),"");
                if(b==true){
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                }else{
                    Toast.makeText(getApplicationContext(),"opps.",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
}

