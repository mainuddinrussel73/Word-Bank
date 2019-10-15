package com.example.mainuddin.myapplication34.ui.promotodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.promotodo.DBproHandle;
import com.example.mainuddin.myapplication34.ui.promotodo.Promotodo_activity;
import com.example.mainuddin.myapplication34.ui.promotodo.promotododata;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

public class promodetail extends AppCompatActivity {

    Button button1;
    TextView textView1,textView2,textView3;
    FloatingActionButton fab1 ;
    boolean toogle =false;
    private CountDownTimer cdTimer;
    private long total = 30000;
    CircularProgressBar circularProgressBar ;
    CircularProgressBar circularProgressBar1,circularProgressBar2,circularProgressBar3,circularProgressBar4 ;
    FABRevealLayout fabRevealLayout;
    promotododata currenttask,oldtask;
    int curr,old;
    FloatingActionButton fab;
    SharedPreferences prefs;
    TextView titletask;
    boolean isfinish = false;
    ExtendedEditText text_field_boxes2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_promodetail);

        fab = findViewById(R.id.fab);
        fab.setActivated(true);
        fab.setImageBitmap(textAsBitmap("OK", 40, Color.WHITE));

        fab.setImageBitmap(textAsBitmap("OK", 40, Color.WHITE));
        try{
            fabRevealLayout = (FABRevealLayout) findViewById(R.id.fab_reveal_layout);
            configureFABReveal(fabRevealLayout);

        }
        catch (Exception e){
            System.out.println(e.getMessage());

        }






        button1 = findViewById(R.id.cancel_task);
        textView1 = findViewById(R.id.procountH);
        textView2 = findViewById(R.id.procountM);
        textView3 = findViewById(R.id.procountS);
        textView1.setText(String.format("%02d", 0));
        textView2.setText(String.format("%02d", 0));
        textView3.setText(String.format("%02d", 30));
        fab1 = findViewById(R.id.fab1);
        circularProgressBar  = findViewById(R.id.circularProgressBar1);
        circularProgressBar.setProgressMax(total);

        text_field_boxes2 = findViewById(R.id.text_field_boxes2);
        titletask = findViewById(R.id.tasktitle);

        circularProgressBar1  = findViewById(R.id.circularProgressBar2);
        circularProgressBar1.setProgressMax(300);
        circularProgressBar2  = findViewById(R.id.circularProgressBar3);
        circularProgressBar2.setProgressMax(300);
        circularProgressBar3  = findViewById(R.id.circularProgressBar4);
        circularProgressBar3.setProgressMax(300);
        circularProgressBar4  = findViewById(R.id.circularProgressBar5);
        circularProgressBar4.setProgressMax(300);

        Intent intent = getIntent();
        curr = intent.getIntExtra("ID",-1);
        currenttask = Promotodo_activity.promotododataList.get(curr);

        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        text_field_boxes2.setText(currenttask.getTitle());

        System.out.println("item   "+ Promotodo_activity.promotododataList.get(curr).getTitle()+"<>"+Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());
        if(currenttask.getNum_of_promotodo()==1){
            fab.setActivated(true);
            // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            System.out.println(currenttask.getNum_of_promotodo());
            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==2){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==3){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==4){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
            //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar4.setProgressWithAnimation(300, (long) 1000);
        }else {
            fab.setActivated(false);
        }

        System.out.println(currenttask.getTitle());

        RelativeLayout relativeLayout = findViewById(R.id.spinerlayout);
        MaterialSpinner spinner = (MaterialSpinner) relativeLayout.findViewById(R.id.spinner);
        spinner.setItems(new Integer(0) ,new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Integer>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Integer item) {
                Snackbar.make(view, "Clicked " + item.intValue(), Snackbar.LENGTH_LONG).show();
                Promotodo_activity.mDBHelper.updateNum(currenttask.getTitle(),item.intValue());
                Promotodo_activity.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                System.out.println("item   "+ Promotodo_activity.promotododataList.get(curr).getTitle()+"<>"+Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());
                if(item.intValue()==1){
                    fab.setActivated(true);
                    // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    System.out.println(item.intValue());
                    circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                    circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==2){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==3){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==4){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar4.setProgressWithAnimation(300, (long) 1000);
                }else {
                    fab.setActivated(false);
                }
            }


        });
    }

    private void configureFABReveal(FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {


            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                try{
                    //prepareBackTransition(fabRevealLayout);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            fabRevealLayout.revealMainView();
                        }
                    });






                    old = prefs.getInt("task",-1);
                    if(old==-1)old = curr;
                    oldtask = Promotodo_activity.promotododataList.get(old);
                    titletask.setText(oldtask.getTitle());
                    if(oldtask.getNum_of_promotodo()!=0){

                        fab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                toogle =!toogle;
                                if(toogle){
                                    fab1.setImageResource(R.drawable.ic_pause_black_24dp);




                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("task", curr);
                                    editor.commit();







                                    startCountDownTimer();
                                }else{
                                    fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                    cdTimer.cancel();
                                }
                            }
                        });
                    }




                    // circularProgressBar.setProgressBarColor(Color.BLACK);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabRevealLayout.revealMainView();
            }
        }, 2000);
    }
    private void startCountDownTimer() {



        cdTimer = new CountDownTimer(total, 1000) {
            public void onTick(long millisUntilFinished) {
                //update total with the remaining time left
                total = millisUntilFinished;



                int seconds = (int) (total / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);


                textView1.setText(String.format("%02d", hours));
                textView2.setText(String.format("%02d", minutes));
                textView3.setText(String.format("%02d", seconds));

                circularProgressBar.setProgressWithAnimation(millisUntilFinished, (long) 1000); // =1s

            }
            public void onFinish() {

                int p=0;
                old = prefs.getInt("task",-1);
                oldtask = Promotodo_activity.promotododataList.get(old);
                Promotodo_activity.mDBHelper.updateNum(oldtask.getTitle(),oldtask.getNum_of_promotodo()-1);
                p = Promotodo_activity.promotododataList.get(old).getNum_of_promotodo()-1;
                Promotodo_activity.promotododataList.get(old).setNum_of_promotodo(p);
                if(p<0){
                    fab.setActivated(false);
                }
                else {


                    textView2.setText(String.format("%02d", 00));
                    textView3.setText(String.format("%02d", 00));
                    textView1.setText(String.format("%02d", 00));

                    circularProgressBar.setProgressWithAnimation(0, (long) 1000);



                    if(curr==old){

                        old = 0;
                    //Promotodo_activity.mDBHelper.updateCompleted(currenttask.getTitle(),1);

                        if(p==1){
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);

                            circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==2){
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);

                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==3){
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(300, (long) 1000);

                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==4){
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(300, (long) 1000);
                        }else {
                            circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                            fab.setActivated(false);
                        }
                    }
                    total = 30000;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("total", total);
                    editor.commit();



                    fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    toogle=!toogle;
                    fabRevealLayout.revealMainView();

                }



            }
        }.start();
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    @Override
    public void onBackPressed() {

        if(total!=30000 && total!=0){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("total", total);
            editor.commit();
            //cdTimer.cancel();
        }


        Intent intent;
        intent = new Intent(this, Promotodo_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }



}
