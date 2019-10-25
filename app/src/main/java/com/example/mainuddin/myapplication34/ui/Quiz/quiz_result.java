package com.example.mainuddin.myapplication34.ui.Quiz;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.words.word_details;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class quiz_result extends AppCompatActivity {

    TextView result;

    ListView listView;
    public  static  List<String> wordBuck = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_quiz_result);
        SharedPreferences  prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        result = findViewById(R.id.result);

        float p_correct = 0,p_ignored = 0,p_wrong = 0;
        float total = (quiz_page.correct+quiz_page.wrong+quiz_page.ignored);
        System.out.println(quiz_page.correct+","+quiz_page.wrong+","+quiz_page.ignored);

        if(quiz_page.correct!=0 && quiz_page.ignored==0 && quiz_page.wrong==0){
            p_correct = (quiz_page.correct/ total)*100;
        }
        if( quiz_page.ignored!=0 && quiz_page.correct==0 && quiz_page.wrong==0){
            p_ignored = (quiz_page.ignored / total)*100;
        }
        if(quiz_page.wrong!=0 && quiz_page.ignored==0 && quiz_page.correct==0){
            p_wrong = (quiz_page.wrong/ total)*100;
        }
        if(quiz_page.ignored!=0 && quiz_page.correct!=0 && quiz_page.wrong==0){
            p_correct = (quiz_page.correct/ total)*100;
            p_ignored = (quiz_page.ignored / total)*100;
        }
        if(quiz_page.ignored!=0 && quiz_page.wrong!=0 && quiz_page.correct==0){
            p_wrong = (quiz_page.wrong/ total)*100;
            p_ignored = (quiz_page.ignored / total)*100;
        }
        if(quiz_page.correct!=0 && quiz_page.wrong!=0 && quiz_page.ignored==0){
            p_correct = (quiz_page.correct/ total)*100;
            p_wrong = (quiz_page.wrong / total)*100;
        }
        if(quiz_page.correct!=0 && quiz_page.wrong!=0 && quiz_page.ignored!=0){
            p_correct = (quiz_page.correct/ total)*100;
            p_ignored = (quiz_page.ignored / total)*100;
            p_wrong = (quiz_page.wrong/ total)*100;
        }
        if(quiz_page.correct==0 && quiz_page.wrong==0 && quiz_page.ignored==0){
            p_ignored = 100;
            p_correct = 0;
            p_wrong = 0;
        }

        System.out.println(p_correct+","+p_wrong+","+p_ignored);

        System.out.println(wordBuck.size());

        ListAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.learnlistitem, wordBuck){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(R.id.label);
                textView.setMaxLines(3);

                /*YOUR CHOICE OF COLOR*/
                if(MainActivity.isDark)
                textView.setTextColor(Color.WHITE);
                else textView.setTextColor(Color.BLACK);

                return view;
            }
        };


        listView = findViewById(R.id.learnlist);

        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.label);
                String text = textView.getText().toString();

                int i = 0;
                for (i = 0; i <MainActivity.contactList.size() ; i++) {
                    if(MainActivity.contactList.get(i).getWORD().equals(text)){
                        System.out.println(MainActivity.contactList.get(i).getWORD());
                        break;
                    }
                }
                System.out.println(i);
                Intent myIntent = new Intent(view.getContext(), word_details.class);
                myIntent.putExtra("message",MainActivity.contactList.get(i).getWORD());
                myIntent.putExtra("meaning",MainActivity.contactList.get(i).getMEANING());
                myIntent.putExtra("id",i);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);

            }
        });


        if(quiz_page.correct!=0 && quiz_page.ignored==0 && quiz_page.wrong==0){
            pieData.add(new SliceValue(p_correct, Color.GREEN).setLabel("Cor"));
        }
        if( quiz_page.ignored!=0 && quiz_page.correct==0 && quiz_page.wrong==0){
            pieData.add(new SliceValue(p_ignored, Color.GRAY).setLabel("Ign"));
        }
        if(quiz_page.wrong!=0 && quiz_page.ignored==0 && quiz_page.correct==0){
            pieData.add(new SliceValue(p_wrong, Color.RED).setLabel("Wro"));
        }
        if(quiz_page.ignored!=0 && quiz_page.correct!=0 && quiz_page.wrong==0){
            pieData.add(new SliceValue(p_ignored, Color.GRAY).setLabel("Ign"));
            pieData.add(new SliceValue(p_correct, Color.GREEN).setLabel("Cor"));
        }
        if(quiz_page.ignored!=0 && quiz_page.wrong!=0 && quiz_page.correct==0){
            pieData.add(new SliceValue(p_ignored, Color.GRAY).setLabel("Ign"));
            pieData.add(new SliceValue(p_wrong, Color.RED).setLabel("Cor"));
        }
        if(quiz_page.correct!=0 && quiz_page.wrong!=0 && quiz_page.ignored==0){
            pieData.add(new SliceValue(p_correct, Color.GREEN).setLabel("Cor"));
            pieData.add(new SliceValue(p_wrong, Color.RED).setLabel("Cor"));
        }
        if(quiz_page.correct==0 && quiz_page.ignored==0 && quiz_page.wrong==0){
            pieData.add(new SliceValue(0, Color.GREEN).setLabel("Cor"));
            pieData.add(new SliceValue(100, Color.GRAY).setLabel("Ign"));
            pieData.add(new SliceValue(0, Color.RED).setLabel("Wro"));
        }
        if(quiz_page.correct!=0 && quiz_page.ignored!=0 && quiz_page.wrong!=0){
            pieData.add(new SliceValue(p_correct, Color.GREEN).setLabel("Cor"));
            pieData.add(new SliceValue(p_ignored, Color.GRAY).setLabel("Ign"));
            pieData.add(new SliceValue(p_wrong, Color.RED).setLabel("Wro"));
        }





        PieChartData pieChartData = new PieChartData(pieData);
        float res = 0;
        if(quiz_page.correct!=0)
        res = (quiz_page.correct/(total))*100;


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);


        RelativeLayout relativeLayout = findViewById(R.id.quiz_res);
        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs1.getBoolean("isDark",false);
        if(isDark){
            result.setBackgroundColor(Color.BLACK);
            result.setTextColor(Color.rgb(255,153,204));
            pieChartData.setHasCenterCircle(true).setCenterText1("Correct : "+df.format(res)+"%").setCenterText1FontSize(10).setCenterText1Color(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.BLACK);
            result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            //textView.setTextColor(Color.WHITE);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.list_viewdark));

            if(wordBuck.size()!=0)listView.setAdapter(adapter);
        }else{
            result.setBackgroundColor(Color.WHITE);
            result.setTextColor(Color.rgb(102,0,51));
            pieChartData.setHasCenterCircle(true).setCenterText1("Correct : "+df.format(res)+"%").setCenterText1FontSize(10).setCenterText1Color(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.WHITE);
            result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            //textView.setTextColor(Color.BLACK);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.listview_border));

            if(wordBuck.size()!=0)listView.setAdapter(adapter);
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
                wordBuck = new ArrayList<>();
                quiz_page.ignored = 0;
                quiz_page.correct = 0;
                quiz_page.wrong = 0;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed(){

    }
}
