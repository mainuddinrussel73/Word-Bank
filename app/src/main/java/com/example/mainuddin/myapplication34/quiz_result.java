package com.example.mainuddin.myapplication34;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.ui.Quiz.quiz_page;
import com.github.mikephil.charting.charts.PieChart;
import com.tapadoo.alerter.Alerter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class quiz_result extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_quiz_result);
        SharedPreferences  prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        result = findViewById(R.id.result);

        float p_correct = 0,p_ignored = 0,p_wrong = 0;
        float total = (quiz_page.correct+quiz_page.wrong+quiz_page.ignored);
        System.out.println(quiz_page.correct+","+quiz_page.wrong+","+quiz_page.ignored);

        if(quiz_page.correct==0){
            p_correct = 0;
        }else if( quiz_page.ignored==0){
            p_ignored = 0;
        } else if(quiz_page.wrong==0){
            p_wrong = 0;
        }else{
            p_correct = (quiz_page.correct/ total)*100;
            p_wrong = (quiz_page.wrong/ total)*100;
            p_ignored = (quiz_page.ignored / total)*100;
        }

        System.out.println(p_correct+","+p_wrong+","+p_ignored);


        pieData.add(new SliceValue(p_correct, Color.GREEN).setLabel("Cor"));
        pieData.add(new SliceValue(p_ignored, Color.GRAY).setLabel("Ign"));
        pieData.add(new SliceValue(p_wrong, Color.RED).setLabel("Wro"));


        PieChartData pieChartData = new PieChartData(pieData);

        float res = (quiz_page.correct/(total))*100;


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);


        RelativeLayout relativeLayout = findViewById(R.id.quiz_res);
        if(MainActivity.isDark){
            result.setBackgroundColor(Color.BLACK);
            result.setTextColor(Color.rgb(255,153,204));
            pieChartData.setHasCenterCircle(true).setCenterText1("Correct : "+df.format(res)+"%").setCenterText1FontSize(10).setCenterText1Color(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.BLACK);
            result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
        }else{
            result.setBackgroundColor(Color.WHITE);
            result.setTextColor(Color.rgb(102,0,51));
            pieChartData.setHasCenterCircle(true).setCenterText1("Correct : "+df.format(res)+"%").setCenterText1FontSize(10).setCenterText1Color(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.WHITE);
            result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
        }



        pieChartView.setPieChartData(pieChartData);


        if(prefs.getInt("highscore", 0)>MainActivity.score){


            Alerter.create(this)
                    .setTitle("CONGRATS!")
                    .setText("New highest score...")
                    .setIcon(R.drawable.ic_insert_emoticon_black_24dp)
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setDuration(5000)
                    .enableSwipeToDismiss() //seems to not work well with OnClickListener
                    .show();


        }


        result.setText(
                "High Score : "+prefs.getInt(
                        "highscore", 0)+"\n"
                        +"Your Score: "+quiz_page.score+"\n"
                        +"Corect : "+quiz_page.correct+"\n"
                        +"Ignored :"+quiz_page.ignored+"\n"
                        +"Wrong : "+quiz_page.wrong);


        Button button = findViewById(R.id.goback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
    }
}
