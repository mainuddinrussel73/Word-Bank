package com.example.mainuddin.myapplication34.ui.promotodo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.media.Constants;
import com.example.mainuddin.myapplication34.ui.media.MyNotificationReceiver;
import com.example.mainuddin.myapplication34.ui.media.NotificationService;
import com.example.mainuddin.myapplication34.ui.promotodo.DBproHandle;
import com.example.mainuddin.myapplication34.ui.promotodo.Promotodo_activity;
import com.example.mainuddin.myapplication34.ui.promotodo.promotododata;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class promodetail extends AppCompatActivity {

    Button button1;
    public  static  TextView textView1,textView2,textView3;
    public  static FloatingActionButton fab1 ;
    public  static  boolean toogle =false;
    private CountDownTimer cdTimer;
    private long total = 1800000;
    public  static  CircularProgressBar circularProgressBar ;
    public  static  CircularProgressBar circularProgressBar1,circularProgressBar2,circularProgressBar3,circularProgressBar4 ;
    public  static  CircularProgressBar circularProgressBar11,circularProgressBar12,circularProgressBar13,circularProgressBar14 ;
    public static FABRevealLayout fabRevealLayout;
    promotododata currenttask;
    public static int curr;
    public static  FloatingActionButton fab;
    public static  TextView titletask;
    boolean isfinish = false;
    public static  ExtendedEditText text_field_boxes2;
    Intent serviceIntent ;
    public  static  SharedPreferences prefs;
    EditText edittext;
    Calendar myCalendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        try{
        setContentView(R.layout.activity_promodetail);}
        catch (Exception e){
            System.out.println(e.getMessage());
        }

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


         prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);




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
        circularProgressBar1.setProgressMax(18000);
        circularProgressBar2  = findViewById(R.id.circularProgressBar3);
        circularProgressBar2.setProgressMax(18000);
        circularProgressBar3  = findViewById(R.id.circularProgressBar4);
        circularProgressBar3.setProgressMax(18000);
        circularProgressBar4  = findViewById(R.id.circularProgressBar5);
        circularProgressBar4.setProgressMax(18000);

        circularProgressBar11  = findViewById(R.id.circularProgressBar01);
        circularProgressBar11.setProgressMax(18000);
        circularProgressBar12  = findViewById(R.id.circularProgressBar11);
        circularProgressBar12.setProgressMax(18000);
        circularProgressBar13  = findViewById(R.id.circularProgressBar12);
        circularProgressBar13.setProgressMax(18000);
        circularProgressBar14  = findViewById(R.id.circularProgressBar13);
        circularProgressBar14.setProgressMax(18000);

        Intent intent = getIntent();
        curr = intent.getIntExtra("ID",-1);
        currenttask = Promotodo_activity.promotododataList.get(curr);

        //prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        text_field_boxes2.setText(currenttask.getTitle());

        System.out.println("item   "+ Promotodo_activity.promotododataList.get(curr).getTitle()+"<>"+Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());
        if(currenttask.getNum_of_promotodo()==1){
            fab.setActivated(true);
            // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            System.out.println(currenttask.getNum_of_promotodo());
            circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==2){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
            // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==3){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
        }else if(currenttask.getNum_of_promotodo()==4){
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
            //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar4.setProgressWithAnimation(18000, (long) 1000);
            circularProgressBar14.setProgressWithAnimation(18000, (long) 1000);
        }else {
            fab.setActivated(false);
        }

        System.out.println(currenttask.getTitle());

        RelativeLayout relativeLayout = findViewById(R.id.spinerlayout);
        MaterialSpinner spinner = (MaterialSpinner) relativeLayout.findViewById(R.id.spinner);
        spinner.setItems(new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        switch (Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo()){
            case 1:
                spinner.setSelectedIndex(0);
                break;
            case 2:
                spinner.setSelectedIndex(1);
                break;
            case 3:
                spinner.setSelectedIndex(2);
                break;
            case 4:
                spinner.setSelectedIndex(3);
                break;

        }

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
                    circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==2){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==3){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgre1ssBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                }else if(item.intValue()==4){
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar4.setProgressWithAnimation(18000, (long) 1000);

                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar14.setProgressWithAnimation(18000, (long) 1000);
                }else {
                    fab.setActivated(false);
                }
            }


        });


        RelativeLayout relativeLayout1 = findViewById(R.id.spinerlayout1);
        MaterialSpinner spinner1 = (MaterialSpinner) relativeLayout1.findViewById(R.id.spinner1);

        TextView textView = findViewById(R.id.isrepeat);

        TextView textView1 = findViewById(R.id.viewdate);

        spinner1.setItems(new Boolean(false),new Boolean(true));

        spinner1.setSelectedIndex(currenttask.ISREPEAT);
        if(currenttask.isIsrepeat()==1)textView.setText("Repeat");
        else textView.setText("No repeat");
        textView1.setText(currenttask.getDue_date()
        );
        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Boolean>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Boolean item) {
                Snackbar.make(view, "Clicked " +currenttask.getTitle()+ item.booleanValue(), Snackbar.LENGTH_LONG).show();
                if(item.booleanValue()==true){
                Promotodo_activity.mDBHelper.updateIsrepeat(currenttask.getTitle(),1);
                    Promotodo_activity.promotododataList.get(curr).setIsrepeat(1);
                }
                else { Promotodo_activity.mDBHelper.updateIsrepeat(currenttask.getTitle(),0);
                    Promotodo_activity.promotododataList.get(curr).setIsrepeat(0);
                }

               // Promotodo_activity.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                if(item.booleanValue()==true)textView.setText("Repeat");
                else textView.setText("No repeat");
               // System.out.println("item   "+ Promotodo_activity.promotododataList.get(curr).getTitle()+"<>"+Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());

            }


        });

        myCalendar = Calendar.getInstance();

        edittext= (EditText) findViewById(R.id.datepick);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(promodetail.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final LinearLayout additem = findViewById(R.id.pro_detail);
        RelativeLayout relativeLayoutr = additem.findViewById(R.id.secondary_view);
      //  RelativeLayout relativeLayout1 = relativeLayout.findViewById(R.id.borderss);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){
           // mListView.setBackgroundColor(Color.BLACK);
            //relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            additem.setBackgroundColor(Color.BLACK);
            spinner.setBackgroundColor(Color.rgb(64,64,64));
            spinner1.setBackgroundColor(Color.rgb(64,64,64));
            edittext.setTextColor(Color.WHITE);
            edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            edittext.setHintTextColor(Color.WHITE);
            relativeLayoutr.setBackgroundColor(Color.BLACK);
            textView.setTextColor(Color.WHITE);
            textView1.setTextColor(Color.WHITE);
            titletask.setTextColor(Color.WHITE);
            text_field_boxes2.setPrefixTextColor(Color.WHITE);
            text_field_boxes2.setTextColor(Color.WHITE);
            text_field_boxes2.setHintTextColor(Color.rgb(185,185,185));
           // tfb.setBackgroundColor(Color.BLACK);
           // tfb.setTextColor(Color.WHITE);
           // tfb.setHintTextColor(Color.rgb(185,185,185));
        }else{
           // mListView.setBackgroundColor(Color.WHITE);
           // relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            additem.setBackgroundColor(Color.WHITE);
            spinner.setBackgroundColor(Color.rgb(185,185,185));
            spinner1.setBackgroundColor(Color.rgb(185,185,185));
            edittext.setHintTextColor(Color.BLACK);
            text_field_boxes2.setTextColor(Color.BLACK);
            edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            text_field_boxes2.setHintTextColor(Color.rgb(64,64,64));
            text_field_boxes2.setPrefixTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            textView1.setTextColor(Color.BLACK);
            edittext.setTextColor(Color.BLACK);
            relativeLayoutr.setBackgroundColor(Color.WHITE);
            titletask.setTextColor(Color.BLACK);
          //  tfb.setBackgroundColor(Color.WHITE);
          ///  tfb.setTextColor(Color.BLACK);
          //  tfb.setHintTextColor(Color.BLACK);
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));

        boolean b = Promotodo_activity.mDBHelper.updateDuedate(currenttask.TITLE,sdf.format(myCalendar.getTime()));
        if(b){
            Toasty.success(promodetail.this,"ss",Toasty.LENGTH_LONG).show();
        }
    }
    private void configureFABReveal(FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {


            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {

                    //prepareBackTransition(fabRevealLayout);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            fabRevealLayout.revealMainView();
                        }
                    });


                if(isMyServiceRunning(Promotodo_service.class)){

                    System.out.println("RUnning.......");
                    toogle = true;
                    fab1.setImageResource(R.drawable.ic_pause_black_24dp);

                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            toogle =!toogle;
                            if(toogle){
                                //SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                                //SharedPreferences.Editor editor;
                                //editor = prefs.edit();
                                //editor.putInt("CURR", curr);
                                //editor.commit();

                                fab1.setImageResource(R.drawable.ic_pause_black_24dp);
                                startService(currenttask);
                                // startCountDownTimer();
                            }else{
                                fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                //cdTimer.cancel();
                                stopService(new Intent(promodetail.this, Promotodo_service.class));

                            }
                        }
                    });

                }else {
                    System.out.println("Not rUnning.......");

                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            toogle =!toogle;
                            if(toogle){
                                //SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor;
                                editor = prefs.edit();
                                editor.putInt("CURR", curr);
                                editor.commit();

                                fab1.setImageResource(R.drawable.ic_pause_black_24dp);
                                startService(currenttask);
                                // startCountDownTimer();
                            }else{
                                fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                //cdTimer.cancel();
                                stopService(new Intent(promodetail.this, Promotodo_service.class));

                            }
                        }
                    });
                }




                    }






        });
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




        Intent intent;
        intent = new Intent(this, Promotodo_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void startService(promotododata currenttask) {

       startService(new Intent(this, Promotodo_service.class));
    }














    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }







}
