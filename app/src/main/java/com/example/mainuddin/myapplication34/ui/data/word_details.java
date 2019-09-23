package com.example.mainuddin.myapplication34.ui.data;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class word_details extends AppCompatActivity {


    private DatabaseHelper mDBHelper;
    private Button update,delete;
    String words,meanings;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_details);
        final Intent intent = getIntent();
        final EditText word = (EditText) findViewById(R.id.word);
        final EditText meaning = (EditText) findViewById(R.id.meaning);
        word.setText(intent.getStringExtra("message"));
        meaning.setText(intent.getStringExtra("meaning"));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        word.setEnabled(false);
        meaning.setEnabled(false);
        mDBHelper = new DatabaseHelper(this);



        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);

        update.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;


                if (i == 1) {
                    word.setEnabled(true);
                    meaning.setEnabled(true);
                } else if (i == 2) {
                    //Double click
                    i = 0;
                    words = word.getText().toString();
                    meanings = meaning.getText().toString();
                    id = intent.getExtras().getInt("id");
                    id++;
                    boolean b = mDBHelper.updateData(String.valueOf(id),words,meanings,"");
                    if(b==true){
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    }else {
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }
                    word.setEnabled(false);
                    meaning.setEnabled(false);
                }


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = intent.getExtras().getInt("id");
                id++;
                int b = mDBHelper.deleteData(String.valueOf(id));
                if(b==1){
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                }else {
                    Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}

