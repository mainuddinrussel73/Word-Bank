package com.example.mainuddin.myapplication34.ui.promotodo;

import android.app.ActivityManager;
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
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.media.Media_list_activity;
import com.example.mainuddin.myapplication34.ui.media.MyNotificationReceiver;
import com.example.mainuddin.myapplication34.ui.media.NotificationService;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView1;
import static com.example.mainuddin.myapplication34.ui.promotodo.Promotodo_service.manager;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar1;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar11;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar12;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar13;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar14;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar2;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar3;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.circularProgressBar4;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.curr;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.fab;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.shake;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.textView1;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.textView2;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.textView3;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.titletask;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.toogle;

public class Promotodo_receiver extends BroadcastReceiver {
    public static int REQUEST_CODE_NOTIFICATION = 1212;


    public static final String GET_TIME = "GET_TIME";
    public static final String SET_TIME = "SET_TIME";



    int ccc = promodetail.prefs.getInt("CURR",0);
    promotododata ccco= Promotodo_activity.promotododataList.get(ccc);

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("action", action);


        if(action.equals(GET_TIME)){
            updateGUI(intent);
        }
        if(action.equals(SET_TIME)){
            Intent service = new Intent(context, Promotodo_service.class);
            context.stopService(service);
            updateGUI(intent);
        }




    }

    private boolean isAppOnForeground(Context context,String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
            }else{
                //App is closed
            }
        }
        return false;
    }

    private void updateGUI(Intent intent) {

        System.out.println("finishd       jjj");



        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);

            int seconds = (int) (millisUntilFinished / 1000);
            int hours = seconds / (60 * 60);
            int tempMint = (seconds - (hours * 60 * 60));
            int minutes = tempMint / 60;
            seconds = tempMint - (minutes * 60);






            titletask.setText(ccco.getTitle());

            Log.i("receiver", "Countdown seconds remaining: " +  millisUntilFinished / 1000);

            if( millisUntilFinished !=0 ) {



                Promotodo_service.notificationView.setTextViewText(R.id.timerview,Integer.toString(hours));
                Promotodo_service.notificationView.setTextViewText(R.id.timerview1,Integer.toString(minutes));
                Promotodo_service.notificationView.setTextViewText(R.id.timerview2,Integer.toString(seconds));
                Promotodo_service.manager.notify(6, Promotodo_service.notificationBuilder.build());

                textView1.setText(String.format("%02d", hours));
                textView2.setText(String.format("%02d", minutes));
                textView3.setText(String.format("%02d", seconds));
                circularProgressBar.setProgressWithAnimation(millisUntilFinished, (long) 1000); // =1s
                fab.setImageBitmap(textAsBitmap(minutes+":"+seconds , 40, Color.WHITE));

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                shake.setInterpolator(interpolator);
                fab.startAnimation(shake);


                if(ccco.getNum_of_promotodo()==1){
                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                }else if(ccco.getNum_of_promotodo()==2){

                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                }else if(ccco.getNum_of_promotodo()==3){
                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
                }else if(ccco.getNum_of_promotodo()==4){
                    circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);
                    circularProgressBar14.setProgressWithAnimation(18000, (long) 1000);
                }else {

                }

            }
            else {

                fab.setImageBitmap(textAsBitmap(00+":"+00 , 40, Color.WHITE));

                //mediaPlayer.stop();
                int p= 0 ;
                Promotodo_activity.mDBHelper.updateNum(ccco.getTitle(),ccco.getNum_of_promotodo()-1);
                Promotodo_activity.mDBHelper.updateCompleted(ccco.getTitle(),ccco.getCompleted_promotodo()+1);
                p = Promotodo_activity.promotododataList.get(ccc).getNum_of_promotodo()-1;
                Promotodo_activity.promotododataList.get(ccc).setNum_of_promotodo(p);
                if(p<0){
                    fab.setActivated(false);
                }
                else {


                    System.out.println("close it");
                    textView2.setText(String.format("%02d", 00));
                    textView3.setText(String.format("%02d", 00));
                    textView1.setText(String.format("%02d", 00));

                    circularProgressBar.setProgressWithAnimation(0, (long) 1000);


                    if(curr==ccc){


                    //Promotodo_activity.mDBHelper.updateCompleted(currenttask.getTitle(),1);

                    if(p==1){
                        fab.setActivated(true);
                        circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);

                        circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                    }else if(p==2){
                        fab.setActivated(true);
                        circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);

                        circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                    }else if(p==3){
                        fab.setActivated(true);
                        circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);

                        circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                    }else if(p==4){
                        fab.setActivated(true);
                        circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
                        circularProgressBar4.setProgressWithAnimation(18000, (long) 1000);
                    }else {
                        circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                        circularProgressBar4.setProgressWithAnimation(0, (long) 1000);
                        fab.setActivated(false);
                    }

                        if(p==1){
                           // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);

                            circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==2){
                           // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);

                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==3){
                           // promodetail.fab.setActivated(true);
                            circularProgressBar11.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(18000, (long) 1000);

                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                        }else if(p==4){
                           // promodetail.fab.setActivated(true);
                            circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
                            circularProgressBar4.setProgressWithAnimation(18000, (long) 1000);
                        }else {
                            circularProgressBar11.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                            circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                            //promodetail.fab.setActivated(false);
                        }
                    }


                    promodetail.fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    toogle=!toogle;
                    promodetail.revealShow(true);


                }



            }
        }
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




}
