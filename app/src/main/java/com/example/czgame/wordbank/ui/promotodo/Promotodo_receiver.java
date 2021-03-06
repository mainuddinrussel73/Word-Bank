package com.example.czgame.wordbank.ui.promotodo;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;
import es.dmoral.toasty.Toasty;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.example.czgame.wordbank.ui.promotodo.Promotodo_service.isStart;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar1;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar11;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar12;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar13;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar14;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar2;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar3;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.circularProgressBar4;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.curr;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.fab;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.prefs;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.shake;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.textView1;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.textView2;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.textView3;
import static com.example.czgame.wordbank.ui.promotodo.promodetail.titletask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Promotodo_receiver extends BroadcastReceiver {
    public static final String GET_TIME = "GET_TIME";
    public static final String SET_TIME = "SET_TIME";
    public static final String PAUSE_TIME = "PAUSE_TIME";
    public static final String RESUME_TIME = "RESUME_TIME";
    public static final String DONE = "DONE";
    public static int REQUEST_CODE_NOTIFICATION = 1212;
    int tp = 0;

    long tike = 18000;
   String end;




    //SharedPreferences  prefs ;

    int ccc = prefs.getInt("CURR", 0);
    promotododata ccco = Promotodo_activity.promotododataList.get(ccc);

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
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();


        Log.e("action", action);

       // System.out.println("receive");
        if (action.equals(GET_TIME)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateGUI(context, intent);
            }
        }
        if (action.equals(SET_TIME)) {
            Intent service = new Intent(context, Promotodo_service.class);
            context.stopService(service);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateGUI(context, intent);
            }
        }
        if(action.equals(PAUSE_TIME)){
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
            if(!taskInfo.get(0).topActivity.getClassName().equals("com.example.czgame.wordbank.ui.promotodo.promodetail")){
                if(Promotodo_service.ispause==false){
                    Promotodo_service.pause();
                    Promotodo_service.notificationView.setImageViewResource(R.id.promo_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    Promotodo_service.notificationView1.setImageViewResource(R.id.promo_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    Promotodo_service.notificationBuilder.setCustomContentView(Promotodo_service.notificationView);
                    Promotodo_service.notificationBuilder.setCustomContentView(Promotodo_service.notificationView1);
                    Promotodo_service.manager.notify(6, Promotodo_service.notificationBuilder.build());
                    Toasty.success(context, "Pause", Toast.LENGTH_SHORT).show();
                }
                else {
                    Promotodo_service.resume(context);
                    Promotodo_service.notificationView.setImageViewResource(R.id.promo_bar_play, R.drawable.ic_pause_black_24dp);
                    Promotodo_service.notificationView1.setImageViewResource(R.id.promo_bar_play, R.drawable.ic_pause_black_24dp);
                    Promotodo_service.notificationBuilder.setCustomContentView(Promotodo_service.notificationView);
                    Promotodo_service.notificationBuilder.setCustomContentView(Promotodo_service.notificationView1);
                    Promotodo_service.manager.notify(6, Promotodo_service.notificationBuilder.build());
                    Toasty.success(context, "Resume", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            //App is closed
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            } else {
                //App is closed
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateGUI(Context context, Intent intent) {

        //System.out.println("finishd       jjj");


        if (intent.getExtras() != null) {

            //  prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


            //isStart = true;
            long millisUntilFinished = intent.getLongExtra("countdown", 0);

            int seconds = (int) (millisUntilFinished / 1000);
            int hours = seconds / (60 * 60);
            int tempMint = (seconds - (hours * 60 * 60));
            int minutes = tempMint / 60;
            seconds = tempMint - (minutes * 60);


            titletask.setText(ccco.getTitle());

            Log.i("receiver", "Countdown seconds remaining: " + millisUntilFinished / 1000);
           // System.out.println(hours);

            if (millisUntilFinished != 0) {


                Promotodo_service.notificationView.setTextViewText(R.id.timerview, Integer.toString(hours));
                Promotodo_service.notificationView.setTextViewText(R.id.timerview1, Integer.toString(minutes));
                Promotodo_service.notificationView.setTextViewText(R.id.timerview2, Integer.toString(seconds));

                Promotodo_service.notificationView1.setTextViewText(R.id.timerview, Integer.toString(hours));
                Promotodo_service.notificationView1.setTextViewText(R.id.timerview1, Integer.toString(minutes));
                Promotodo_service.notificationView1.setTextViewText(R.id.timerview2, Integer.toString(seconds));


                Promotodo_service.manager.notify(6, Promotodo_service.notificationBuilder.build());

                textView1.setText(String.format("%02d", hours));
                textView2.setText(String.format("%02d", minutes));
                textView3.setText(String.format("%02d", seconds));
                circularProgressBar.setProgressWithAnimation(millisUntilFinished, (long) 1000); // =1s
                fab.setImageBitmap(textAsBitmap(minutes + ":" + seconds, 40, Color.WHITE));

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                shake.setInterpolator(interpolator);
                fab.startAnimation(shake);


                if (ccco.getNum_of_promotodo() == 1) {
                    circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                } else if (ccco.getNum_of_promotodo() == 2) {

                    circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                } else if (ccco.getNum_of_promotodo() == 3) {
                    circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                } else if (ccco.getNum_of_promotodo() == 4) {
                    circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar14.setProgressWithAnimation(tike, (long) 1000);
                } else {

                }

            } else {


                //context.stopService(new Intent(context, Promotodo_service.class));
                //context.unregisterReceiver(Promotodo_receiver.this);
                Promotodo_service.cdt.cancel();
                //context.stopService(intent);
                fab.setImageBitmap(textAsBitmap(00 + ":" + 00, 40, Color.WHITE));

                //mediaPlayer.stop();


                int p = 0;

                int k = Promotodo_activity.promotododataList.get(ccc).ISREPEAT;
                String s = Promotodo_activity.promotododataList.get(ccc).TITLE;

                String d = Promotodo_activity.promotododataList.get(ccc).getDue_date();
                //int c = Promotodo_activity.promotododataList.get(ccc).getCompleted_promotodo();
                //int n = Promotodo_activity.promotododataList.get(ccc).NUM;

                Promotodo_activity.mDBHelper.updateNum(ccco.getTitle(), ccco.getNum_of_promotodo() - 1);
                Promotodo_activity.mDBHelper.updateCompleted(ccco.getTitle(), ccco.getCompleted_promotodo() + 1);
                p = Promotodo_activity.promotododataList.get(ccc).getNum_of_promotodo() - 1;
                Promotodo_activity.promotododataList.get(ccc).setNum_of_promotodo(p);


                //int sub = prefs.getInt("t",0);


                int t = Promotodo_activity.promotododataList.get(ccc).getCompleted_promotodo() + 1;


                Promotodo_activity.promotododataList.get(ccc).setCompleted_promotodo(t);


                int c = Promotodo_activity.promotododataList.get(ccc).getCompleted_promotodo();
                int n = c + Promotodo_activity.promotododataList.get(ccc).NUM;

                if (p <= 0) {


                    fab.setActivated(false);


                    c = 0;

                    int sub = prefs.getInt("t", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("t", sub + 1);
                    editor.commit();


                    // System.out.println(k + "isrepeat");
                    promotododata promotododata = Promotodo_activity.promotododataList.get(ccc);
                    int i = Promotodo_activity.mDBHelper.deleteData(Promotodo_activity.promotododataList.get(ccc).getTitle());
                    Promotodo_activity.promotododataList.remove(ccc);
                    if (k == 1) {
                        LocalDate parsedDate = LocalDate.parse(d); //Parse date from String
                        LocalDate addedDate = parsedDate.plusDays(1);
                        String str = addedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        boolean b = Promotodo_activity.mDBHelper.insertAll(s, k, n, str, c);
                        if (b == true) {
                            //Toasty.success(intent.get(), "Repeated.", Toast.LENGTH_SHORT).show();
                            promotododata.setDue_date(str);
                            promotododata.setNum_of_promotodo(0);
                            Promotodo_activity.promotododataList.add(Promotodo_activity.promotododataList.size(), promotododata);
                            //Promotodo_activity.mAdapter.notifyDataSetChanged();
                            Toasty.success(context, "Repeated.", Toast.LENGTH_SHORT).show();


                        } else {
                            Toasty.error(context, "opps.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //promodetail.fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    // toogle=!toogle;

                    Promotodo_service.ispause = true;
                    Intent myIntent = new Intent();


                    try {
                        myIntent.setClassName(context.getApplicationContext().getPackageName(), "Promotodo_activity");
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(myIntent);
                    } catch (Exception e) {
                       e.getStackTrace();
                    }


                } else {

                    int sub = prefs.getInt("t", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("t", sub + 1);
                    editor.commit();

                    System.out.println("close it");
                    textView2.setText(String.format("%02d", 00));
                    textView3.setText(String.format("%02d", 00));
                    textView1.setText(String.format("%02d", 00));

                    circularProgressBar.setProgressWithAnimation(0, (long) 1000);


                    if (curr == ccc) {


                        //Promotodo_activity.mDBHelper.updateCompleted(currenttask.getTitle(),1);

                        if (p == 1) {
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 2) {
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 3) {
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 4) {
                            fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);
                        } else {
                            circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                            fab.setActivated(false);
                        }

                        if (p == 1) {
                            // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 2) {
                            // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 3) {
                            // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);

                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        } else if (p == 4) {
                            // promodetail.fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);
                        } else {
                            circularProgressBar11.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                            //promodetail.fab.setActivated(false);
                        }
                    }


                    ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());

                    Promotodo_service.ispause = true;
                    //promodetail.fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    //toogle=!toogle;
                    if (taskInfo.get(0).topActivity.getClassName().equals("com.example.czgame.wordbank.ui.promotodo.promodetail") && !promodetail.toogleview) {

                        promodetail.revealShow(false);

                    }


                }

                //done.setEnabled(true);
                DbTaskDhandle mDBHelper = new DbTaskDhandle(context);
                LocalDate parsedDate = LocalDate.now(); //Parse date from String

                isStart = true;
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                String time = localTime.format(dateTimeFormatter);
                end = time;
                System.out.println(Promotodo_service.start);
                System.out.println(end);
                String str = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                boolean b = mDBHelper.insertAll(ccco.TITLE,
                        ccco.ISREPEAT
                        ,ccco.NUM,str
                        ,ccco.COMPLETED,Promotodo_service.start,end);

            }
        }
    }

    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Promotodo_service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    private boolean isAppRunning(Context context) {
        ActivityManager m = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n = 0;
        while (itr.hasNext()) {
            n++;
            itr.next();
        }
        // App is killed
        return n != 1;// App is in background or foreground
    }


}
