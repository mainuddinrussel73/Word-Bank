package com.example.mainuddin.myapplication34.ui.data;

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
import com.example.mainuddin.myapplication34.news_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class news_update  extends AppCompatActivity {


    private DBNewsHelper mDBHelper;
    private Button update,delete;
    String titles,bodies;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_update);
        final Intent intent = getIntent();
        final EditText title = (EditText) findViewById(R.id.title2);
        final EditText body = (EditText) findViewById(R.id.body2);

        title.setText(intent.getStringExtra("title"));
        body.setText(intent.getStringExtra("body"));

        LinearLayout additem = findViewById(R.id.update_news_item);

        if(MainActivity.isDark){
            additem.setBackgroundColor(Color.rgb(64,64,64));
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            title.setTextColor(Color.WHITE);
            body.setTextColor(Color.WHITE);
        }else{
            additem.setBackgroundColor(Color.WHITE);
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            title.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
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
                    id = intent.getExtras().getInt("id");
                    id++;
                    boolean b = mDBHelper.updateData(String.valueOf(id),intent.getStringExtra("title"),titles,bodies);
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
