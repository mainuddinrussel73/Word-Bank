package com.example.mainuddin.myapplication34.ui.insert;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import es.dmoral.toasty.Toasty;

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
        final LinearLayout additem = findViewById(R.id.add_item);

        if(MainActivity.isDark){
            additem.setBackgroundColor(Color.rgb(64,64,64));
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            word.setHintTextColor(Color.rgb(185,185,185));
            meaning.setHintTextColor(Color.rgb(185,185,185));
            word.setTextColor(Color.rgb(185,185,185));
            meaning.setTextColor(Color.rgb(185,185,185));
        }else{
            additem.setBackgroundColor(Color.WHITE);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            meaning.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            word.setHintTextColor(Color.rgb(64,64,64));
            meaning.setHintTextColor(Color.rgb(64,64,64));
            word.setTextColor(Color.rgb(64,64,64));
            meaning.setTextColor(Color.rgb(64,64,64));
        }


        word.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if(!arg1){
                     DatabaseHelper mDBHelper = new DatabaseHelper(add_page.this);;
                    // SQLiteDatabase mDb;
                    String id = "-1";
                    //SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
                    try{
                        Cursor re  = db1.rawQuery("SELECT * FROM Word_table WHERE WORD = ?; ", new String[] {word.getText().toString()});
                        if (re.moveToFirst()) {
                            do {
                                System.out.println(re.getString(0));
                                id =  re.getString(0);
                            } while (re.moveToNext());
                        }

                        re.close();
                        // System.out.println(re.getString(0));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(!id.equals("-1")){
                        Toasty.warning(add_page.this,"Data already exist",Toasty.LENGTH_LONG).show();
                        done.setEnabled(false);
                    }else{
                        done.setEnabled(true);
                    }
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                if(word.getText().toString().isEmpty() || word.getText().toString().trim().length()<=0){
                    Toasty.error(getApplicationContext(),"No input.",Toast.LENGTH_SHORT).show();
                }else if(meaning.getText().toString().isEmpty() || meaning.getText().toString().trim().length()<=0){
                    Toasty.error(getApplicationContext(),"No input.",Toast.LENGTH_SHORT).show();
                }

                else{
                    boolean b = mDBHelper.insertData(word.getText().toString(),meaning.getText().toString(),"");
                    if(b==true){
                        Toasty.success(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    }else{
                        Toasty.error(getApplicationContext(),"opps.",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }
}

