package com.example.czgame.wordbank.root;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.czgame.wordbank.BuildConfig;
import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class WalkThrough extends FancyWalkthroughActivity {

    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String CUSTOM_ACTION = "custom_action";
    public  static boolean isfirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkFirstRun()) {
            FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard("Collect Words", "Your can store unknown words, which you face daily.", R.drawable.third);
            FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("Collect Notes With Image", "Make daily notes for future reading.", R.drawable.second);
            FancyWalkthroughCard fancywalkthroughCard3 = new FancyWalkthroughCard("Listen Music", "You can listen music while studying.", R.drawable.first);
            FancyWalkthroughCard fancywalkthroughCard4 = new FancyWalkthroughCard("Follow Promotodo", "You can maintain promodo for a better focus on time.", R.drawable.four);

            fancywalkthroughCard1.setBackgroundColor(R.color.white);
            fancywalkthroughCard1.setIconLayoutParams(1800, 1200, 0, 0, 0, 0);
            fancywalkthroughCard2.setBackgroundColor(R.color.white);
            fancywalkthroughCard2.setIconLayoutParams(1800, 1200, 0, 0, 0, 0);
            fancywalkthroughCard3.setBackgroundColor(R.color.white);
            fancywalkthroughCard3.setIconLayoutParams(1800, 1200, 0, 0, 0, 0);
            fancywalkthroughCard4.setBackgroundColor(R.color.white);
            fancywalkthroughCard4.setIconLayoutParams(1800, 1200, 0, 0, 0, 0);
            List<FancyWalkthroughCard> pages = new ArrayList<>();

            pages.add(fancywalkthroughCard1);
            pages.add(fancywalkthroughCard2);
            pages.add(fancywalkthroughCard3);
            pages.add(fancywalkthroughCard4);

            for (FancyWalkthroughCard page : pages) {
                page.setTitleColor(R.color.black);
                page.setDescriptionColor(R.color.black);
            }
            setFinishButtonTitle("Get Started");
            showNavigationControls(true);
            setColorBackground(R.color.colorPrimary);
            //setImageBackground(R.drawable.first);
            setInactiveIndicatorColor(R.color.grey_600);
            setActiveIndicatorColor(R.color.colorPrimary);
            setOnboardPages(pages);
        }else{
            Intent i = new Intent(WalkThrough.this, MainActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onFinishButtonPressed() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        } else {

        }
        Intent i = new Intent(WalkThrough.this, MainActivity.class);
        startActivity(i);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(WalkThrough.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(WalkThrough.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.info(WalkThrough.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(WalkThrough.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    private boolean checkFirstRun() {

        SharedPreferences appPreferences = getSharedPreferences("MyAPP", 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt("app_first_time", 0);

        //Log.d("appPreferences", "app_first_time = " + appLastBuildVersion);

        if (appLastBuildVersion == appCurrentBuildVersion ) {
            isfirst = false;
            return false; //ya has iniciado la appp alguna vez

        } else {
            appPreferences.edit().putInt("app_first_time",
                    appCurrentBuildVersion).apply();
            if (appLastBuildVersion == 0) {
                isfirst = true;
                return true; //es la primera vez

            } else {
                isfirst = false;
                return false; //es una versión nueva
            }
        }
    }

}
