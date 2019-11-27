package com.example.czgame.wordbank.ui.promotodo;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.czgame.wordbank.R;

import androidx.appcompat.app.AppCompatActivity;

public class tree extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);


        ImageView imageView = findViewById(R.id.CorrWrong);
        Glide.with(this)
                .load(R.drawable.total)
                .into(imageView);
    }
}
