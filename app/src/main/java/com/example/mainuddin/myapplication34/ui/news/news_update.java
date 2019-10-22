package com.example.mainuddin.myapplication34.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class news_update  extends AppCompatActivity {


    private DBNewsHelper mDBHelper;
    private Button update,delete;
    String titles,bodies,urls;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_update);
        final Intent intent = getIntent();
        final EditText title = (EditText) findViewById(R.id.title2);
        final EditText body = (EditText) findViewById(R.id.body2);
        final EditText url = (EditText) findViewById(R.id.url2);

        title.setText(intent.getStringExtra("title"));
        body.setText(intent.getStringExtra("body"));
        url.setText(intent.getStringExtra("url"));

        LinearLayout additem = findViewById(R.id.update_news_item);
        title.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){
            additem.setBackgroundColor(Color.BLACK);
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            url.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            title.setTextColor(Color.WHITE);
            body.setTextColor(Color.WHITE);
            url.setTextColor(Color.WHITE);
        }else{
            additem.setBackgroundColor(Color.WHITE);
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            url.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            title.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
            url.setTextColor(Color.BLACK);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDBHelper = new DBNewsHelper(this);



        update = (Button) findViewById(R.id.save1);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    titles = title.getText().toString();
                    bodies = body.getText().toString();
                    urls = url.getText().toString();

                    id = intent.getExtras().getInt("id");
                    id++;
                boolean b;
                if(urls.isEmpty()){
                    b = mDBHelper.updateDatau(String.valueOf(id),intent.getStringExtra("title"),titles,bodies);
                    }
                    else  b = mDBHelper.updateData(String.valueOf(id),intent.getStringExtra("title"),titles,bodies,urls);
                    if(b==true){
                        Toasty.success(getApplicationContext(),"Done.", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), news_activity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    }else {
                        Toasty.error(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }


}

