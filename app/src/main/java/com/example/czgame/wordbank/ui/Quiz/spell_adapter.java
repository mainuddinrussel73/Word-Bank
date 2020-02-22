package com.example.czgame.wordbank.ui.Quiz;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.word;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class spell_adapter extends RecyclerView.Adapter<spell_adapter.MyViewHolder>  {
    word studentDataList;

    public spell_adapter(word studentDataList) {
        this.studentDataList = studentDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.spell_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, int position) {


            word data = studentDataList;
            Random rnd = new Random();
            int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            viewHolder.parent.setBackgroundColor(currentColor);
            viewHolder.meaningB.setText(data.getMEANINGB());
            viewHolder.meaningE.setText(String.valueOf(data.getMEANINGE()));

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView meaningB, meaningE;
        LinearLayout parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            meaningB = itemView.findViewById(R.id.meaningB);
            meaningE = itemView.findViewById(R.id.meaningE);
        }
    }
}