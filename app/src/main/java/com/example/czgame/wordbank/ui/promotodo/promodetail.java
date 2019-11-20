package com.example.czgame.wordbank.ui.promotodo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.suke.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class promodetail extends AppCompatActivity {

    public static TextView textView1, textView2, textView3;
    public static FloatingActionButton fab1, fab2, fab3;
    public static boolean toogle = false;
    public static CircularProgressBar circularProgressBar;
    public static CircularProgressBar circularProgressBar1, circularProgressBar2, circularProgressBar3, circularProgressBar4;
    public static CircularProgressBar circularProgressBar11, circularProgressBar12, circularProgressBar13, circularProgressBar14;
    public static int curr;
    public static FloatingActionButton fab;
    public static TextView titletask;
    public static ExtendedEditText text_field_boxes2;
    public static SharedPreferences prefs;
    public static Animation shake;
    public static MyBounceInterpolator interpolator;
    public static View view;
    public static View dialogView;
    public static boolean toogleview = false;
    public static Dialog dialog;
    Button button1;
    long tike = 18000;
    // public static FABRevealLayout fabRevealLayout;
    promotododata currenttask;
    boolean isfinish = false;
    Intent serviceIntent;
    Button edittext,whitenoise;
    Calendar myCalendar;
    MediaPlayer mediaPlayer;
    TextView textViewss,shownoise;
    private CountDownTimer cdTimer;
    private long total = 1800000;

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

    public static void revealShow(boolean b) {


        toogleview = true;
        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab.getX() + (fab.getWidth() / 2));
        int cy = (int) (fab.getY()) + fab.getHeight() + 56;


        if (b) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);

            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //dialog.create();
                    view.setVisibility(View.GONE);


                }
            });

            revealAnimator.setDuration(700);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);


                }
            });
            anim.setDuration(700);
            anim.start();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        try {
            setContentView(R.layout.activity_promodetail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        dialogView = View.inflate(this, R.layout.content_promosec, null);
        view = dialogView.findViewById(R.id.secondary_view);
        dialog = new Dialog(this, R.style.MyAlertDialogStyle);
        fab = findViewById(R.id.fab);
        fab.setActivated(true);

        fab.setImageBitmap(textAsBitmap(00 + ":" + 00, 40, Color.WHITE));

        textViewss = findViewById(R.id.viewdate);

        shake = AnimationUtils.loadAnimation(this, R.anim.glowing);
        // fab.setImageBitmap(textAsBitmap("OK", 40, Color.WHITE));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSecond();
            }
        });


        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);




        text_field_boxes2 = findViewById(R.id.text_field_boxes2);


        circularProgressBar1 = findViewById(R.id.circularProgressBar2);
        circularProgressBar1.setProgressMax(tike);
        circularProgressBar2 = findViewById(R.id.circularProgressBar3);
        circularProgressBar2.setProgressMax(tike);
        circularProgressBar3 = findViewById(R.id.circularProgressBar4);
        circularProgressBar3.setProgressMax(tike);
        circularProgressBar4 = findViewById(R.id.circularProgressBar5);
        circularProgressBar4.setProgressMax(tike);


        Intent intent = getIntent();
        curr = intent.getIntExtra("ID", -1);
        currenttask = Promotodo_activity.promotododataList.get(curr);

        //prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        text_field_boxes2.setText(currenttask.getTitle());

        System.out.println("item   " + Promotodo_activity.promotododataList.get(curr).getTitle() + "<>" + Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());
        if (currenttask.getNum_of_promotodo() == 1) {
            fab.setActivated(true);
            // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            System.out.println(currenttask.getNum_of_promotodo());
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 2) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 3) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 4) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar14.setProgressWithAnimation(tike, (long) 1000);
        } else {
            fab.setActivated(false);
        }

        System.out.println(currenttask.getTitle());

        RelativeLayout relativeLayout = findViewById(R.id.spinerlayout);
        MaterialSpinner spinner = relativeLayout.findViewById(R.id.spinner);
        spinner.setItems(new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        switch (Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo()) {
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
                Promotodo_activity.mDBHelper.updateNum(currenttask.getTitle(), item.intValue());
                Promotodo_activity.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                Promotodo_activity.mDBHelper.updateCompleted(currenttask.getTitle(), 0);
                Promotodo_activity.promotododataList.get(curr).setCompleted_promotodo(0);

                System.out.println("item   " + Promotodo_activity.promotododataList.get(curr).getTitle() + "<>" + Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());
                if (item.intValue() == 1) {
                    fab.setActivated(true);
                    // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    System.out.println(item.intValue());
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //  circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 2) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 3) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgre1ssBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 4) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar14.setProgressWithAnimation(tike, (long) 1000);
                } else {
                    fab.setActivated(false);
                }
            }


        });


        com.suke.widget.SwitchButton switchButton = findViewById(R.id.spinner1);

        TextView textView = findViewById(R.id.isrepeat);


        if (currenttask.isIsrepeat() == 1) {
            textView.setText("Repeat");
            switchButton.setChecked(true);
        } else {
            textView.setText("No repeat");
            switchButton.setChecked(false);
        }

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job

                Snackbar.make(view, "Clicked " + currenttask.getTitle() + isChecked, Snackbar.LENGTH_LONG).show();
                if (isChecked == true) {
                    Promotodo_activity.mDBHelper.updateIsrepeat(currenttask.getTitle(), 1);
                    Promotodo_activity.promotododataList.get(curr).setIsrepeat(1);
                } else {
                    Promotodo_activity.mDBHelper.updateIsrepeat(currenttask.getTitle(), 0);
                    Promotodo_activity.promotododataList.get(curr).setIsrepeat(0);
                }

                // Promotodo_activity.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                if (isChecked == true) textView.setText("Repeat");
                else textView.setText("No repeat");
            }
        });


        textViewss.setText(currenttask.getDue_date());

        myCalendar = Calendar.getInstance();

        edittext = findViewById(R.id.datepick);
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

        whitenoise = findViewById(R.id.soundpick);
        shownoise = findViewById(R.id.viewsound);
        shownoise.setText(Promotodo_service.fileName);
        boolean finalIsDark = isDark;
        whitenoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext(), R.style.DialogueLight);
                if (finalIsDark) {

                    if (finalIsDark) {
                        b = new AlertDialog.Builder(view.getContext(), R.style.DialogurDark);
                    } else {
                        b = new AlertDialog.Builder(view.getContext(), R.style.DialogueLight);
                    }
                }

                b.setTitle("White Noise.");
                String[] types = {"Rain", "Ticking", "Ocean Shore","None"};
                b.setItems(types, (dialog, which) -> {

                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            Promotodo_service.fileName = "rain";
                            shownoise.setText(Promotodo_service.fileName);
                            break;
                        case 1:

                            Promotodo_service.fileName = "ticking";
                            shownoise.setText(Promotodo_service.fileName);

                            break;
                        case 2:
                            Promotodo_service.fileName = "oceanshore";
                            shownoise.setText(Promotodo_service.fileName);
                            break;
                        case 3:
                            Promotodo_service.fileName = "none";
                            shownoise.setText(Promotodo_service.fileName);
                            break;
                    }
                });
                b.show();


            }
        });

        RelativeLayout additem = findViewById(R.id.pro_detail);
        RelativeLayout relativeLayoutr = dialogView.findViewById(R.id.secondary_view);
        RelativeLayout relativeLayout1 = findViewById(R.id.belowhere);

        TextView place = findViewById(R.id.place);
        TextView place1 = findViewById(R.id.place1);
        TextView texts = findViewById(R.id.texts);

        //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            // mListView.setBackgroundColor(Color.BLACK);
            //relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            additem.setBackgroundColor(Color.BLACK);
            place.setTextColor(Color.WHITE);
            place1.setTextColor(Color.WHITE);

            texts.setTextColor(Color.WHITE);
            texts.setHintTextColor(Color.rgb(185, 185, 185));
            texts.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.backgroundborder));

            shownoise.setTextColor(Color.WHITE);

            //fab1.setBackgroundColor(Color.RED);
            spinner.setBackgroundColor(Color.rgb(64, 64, 64));
            // spinner1.setBackgroundColor(Color.rgb(64,64,64));
            // edittext.setTextColor(Color.WHITE);
            //edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            //edittext.setHintTextColor(Color.WHITE);
            relativeLayoutr.setBackgroundColor(Color.rgb(255, 69, 0));
            textView.setTextColor(Color.WHITE);
            textViewss.setTextColor(Color.WHITE);
            //titletask.setTextColor(Color.WHITE);
            text_field_boxes2.setPrefixTextColor(Color.WHITE);
            text_field_boxes2.setTextColor(Color.WHITE);
            text_field_boxes2.setHintTextColor(Color.rgb(185, 185, 185));
            // tfb.setBackgroundColor(Color.BLACK);
            // tfb.setTextColor(Color.WHITE);
            // tfb.setHintTextColor(Color.rgb(185,185,185));
        } else {
            place.setTextColor(Color.BLACK);
            place1.setTextColor(Color.BLACK);

            texts.setTextColor(Color.BLACK);
            texts.setHintTextColor(Color.BLACK);
            texts.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.backgroundborder));

            shownoise.setTextColor(Color.BLACK);
            // mListView.setBackgroundColor(Color.WHITE);
            // relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            additem.setBackgroundColor(Color.WHITE);
            //fab1.setBackgroundColor(Color.RED);
            spinner.setBackgroundColor(Color.rgb(185, 185, 185));
            //spinner1.setBackgroundColor(Color.rgb(185,185,185));
            // edittext.setHintTextColor(Color.BLACK);
            text_field_boxes2.setTextColor(Color.BLACK);
            //edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            text_field_boxes2.setHintTextColor(Color.rgb(64, 64, 64));
            text_field_boxes2.setPrefixTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            textViewss.setTextColor(Color.BLACK);
            // edittext.setTextColor(Color.BLACK);
            relativeLayoutr.setBackgroundColor(Color.rgb(255, 69, 0));
            //titletask.setTextColor(Color.BLACK);
            //  tfb.setBackgroundColor(Color.WHITE);
            ///  tfb.setTextColor(Color.BLACK);
            //  tfb.setHintTextColor(Color.BLACK);
        }

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        try {
            textViewss.setText(sdf.format(myCalendar.getTime()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        boolean b = Promotodo_activity.mDBHelper.updateDuedate(currenttask.TITLE, sdf.format(myCalendar.getTime()));
        if (b) {
            Toasty.success(promodetail.this, "ss", Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {


        Intent intent;
        intent = new Intent(this, Promotodo_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    private void showSecond() {


        toogleview = false;

        dialogView = View.inflate(this, R.layout.content_promosec, null);
        view = dialogView.findViewById(R.id.secondary_view);
        dialog = new Dialog(this, R.style.MyAlertDialogStyle);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            dialog.setContentView(dialogView);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        Button imageView = dialogView.findViewById(R.id.cancel_task);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(false);
            }
        });

        fab1 = dialogView.findViewById(R.id.fab16);
        fab2 = dialogView.findViewById(R.id.fab24);
        fab3 = dialogView.findViewById(R.id.fab3);


        button1 = dialogView.findViewById(R.id.cancel_task);
        textView1 = dialogView.findViewById(R.id.procountH);
        textView2 = dialogView.findViewById(R.id.procountM);
        textView3 = dialogView.findViewById(R.id.procountS);

        titletask = dialogView.findViewById(R.id.tasktitle);


        textView1.setText(String.format("%02d", 30));
        textView2.setText(String.format("%02d", 00));
        textView3.setText(String.format("%02d", 00));

        circularProgressBar11 = dialogView.findViewById(R.id.circularProgressBar01);
        circularProgressBar11.setProgressMax(tike);
        circularProgressBar12 = dialogView.findViewById(R.id.circularProgressBar11);
        circularProgressBar12.setProgressMax(tike);
        circularProgressBar13 = dialogView.findViewById(R.id.circularProgressBar12);
        circularProgressBar13.setProgressMax(tike);
        circularProgressBar14 = dialogView.findViewById(R.id.circularProgressBar13);
        circularProgressBar14.setProgressMax(tike);

        circularProgressBar = dialogView.findViewById(R.id.circularProgressBar1);
        circularProgressBar.setProgressMax(total);

        // RelativeLayout additem = findViewById(R.id.pro_detail);
        RelativeLayout relativeLayoutr = dialogView.findViewById(R.id.secondary_view);
        //  RelativeLayout relativeLayout1 = relativeLayout.findViewById(R.id.borderss);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            // mListView.setBackgroundColor(Color.BLACK);
            //relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            // additem.setBackgroundColor(Color.BLACK);
            //fab1.setBackgroundColor(Color.RED);
            // relativeLayoutr.setBackgroundColor(Color.A400red);
            //textView1.setTextColor(Color.WHITE);
            // tfb.setBackgroundColor(Color.BLACK);
            // tfb.setTextColor(Color.WHITE);
            // tfb.setHintTextColor(Color.rgb(185,185,185));
        } else {
            // mListView.setBackgroundColor(Color.WHITE);
            // relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            //additem.setBackgroundColor(Color.WHITE);
            //textView1.setTextColor(Color.BLACK);
            //edittext.setTextColor(Color.BLACK);
            // relativeLayoutr.setBackgroundColor(Color.rgb(255, 51, 0));
            //titletask.setTextColor(Color.BLACK);
            //  tfb.setBackgroundColor(Color.WHITE);
            ///  tfb.setTextColor(Color.BLACK);
            //  tfb.setHintTextColor(Color.BLACK);
        }


        // System.out.println("item   "+ Promotodo_activity.promotododataList.get(curr).getTitle()+"<>"+Promotodo_activity.promotododataList.get(curr).getNum_of_promotodo());


        final int width = dialogView.getWidth();
        float positionX = fab1.getPivotX();
        float posiitonY = fab1.getPivotY();
        int translationX = (int) ((-(width - fab1.getWidth()) / 2f));


        if (isMyServiceRunning(Promotodo_service.class)) {


            System.out.println("RUnning.......");
            //toogle = true;
            //fab1.setImageResource(R.drawable.ic_pause_black_24dp);
            if (!Promotodo_service.ispause) {
                fab1.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
            } else {
                fab1.setVisibility(View.VISIBLE);
                int ccc = promodetail.prefs.getInt("CURR", 0);
                promotododata ccco = Promotodo_activity.promotododataList.get(ccc);

                if (ccco.getNum_of_promotodo() <= 0) {
                    fab1.setVisibility(View.INVISIBLE);
                } else {
                    fab1.setVisibility(View.VISIBLE);
                }
                fab2.setVisibility(View.INVISIBLE);
                fab3.setVisibility(View.INVISIBLE);
            }

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //fab1.setImageResource(R.drawable.ic_pause_black_24dp);


                    Promotodo_service.resume(promodetail.this);


                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);

                    // startCountDownTimer();

                    //fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    //cdTimer.cancel();


                }
            });
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Promotodo_service.pause();
                    //stopService(new Intent(promodetail.this, Promotodo_service.class));
                    // mediaPlayer.pause();


                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
            });

            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Promotodo_service.pause();


                    Promotodo_service.total = 1800000;

                    Promotodo_service.ispause = true;
                    stopService(new Intent(promodetail.this, Promotodo_service.class));
                    // mediaPlayer.pause();

                    Intent myIntent = new Intent(view.getContext(), Promotodo_activity.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                    // revealShow(true);


                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            if (!Promotodo_service.ispause) {
                fab1.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
            } else {
                fab1.setVisibility(View.VISIBLE);

                if (currenttask.getNum_of_promotodo() <= 0) {
                    fab1.setVisibility(View.INVISIBLE);
                } else {
                    fab1.setVisibility(View.VISIBLE);
                }
                fab2.setVisibility(View.INVISIBLE);
                fab3.setVisibility(View.INVISIBLE);
            }

            System.out.println("Not rUnning.......");

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    SharedPreferences.Editor editor;
                    editor = prefs.edit();
                    editor.putInt("CURR", curr);
                    editor.commit();

                    startService();
                    Promotodo_service.ispause = false;
                    // revealShow(true);

                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    // startCountDownTimer();


                }
            });

            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Promotodo_service.pause();

                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);

                }
            });


        }


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }

    public void startService() {


        Intent resultBroadCastIntent = new Intent("android.intent.action.BOOT_COMPLETED");
        resultBroadCastIntent.setAction("GET_TIME");
        sendBroadcast(resultBroadCastIntent);
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

class MyBounceInterpolator implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
