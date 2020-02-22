package com.example.czgame.wordbank.ui.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.czgame.wordbank.R;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.stfalcon.bottomtablayout.BottomTabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.example.czgame.wordbank.ui.words.MainActivity.isDark;

public class music_base extends AppCompatActivity {

    public static BottomTabLayout bottomTabLayout;
    public static  Button button;
    public  static  Context context;
    public static Activity activity;
    public  static Toolbar toolbar;
    protected OnBackPressedListener onBackPressedListener;
    private int container;
    public  static WaveVisualizer mVisualizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_music_base);


         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.getNavigationIcon().setTint(Color.parseColor("#DF0974"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        container = R.id.container; //Container for fragments

        //Setup button tab layout
        bottomTabLayout = findViewById(R.id.bottomTabLayout);
        //set button text style
        bottomTabLayout.setButtonTextStyle(R.style.TabButtonTextStyle);
        // set buttons from menu resource
        bottomTabLayout.setItems(R.menu.bottom_menu);
        //set on selected tab listener.
        bottomTabLayout.setListener(new BottomTabLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switchFragment(id);
            }
        });
        //set button that will be select on start activity
        bottomTabLayout.setSelectedTab(R.id.menu_button1);
        //enable indicator
        bottomTabLayout.setIndicatorVisible(true);
        //indicator height
        bottomTabLayout.setIndicatorHeight(getResources().getDimension(R.dimen.indicator_height));
        if(!isDark)
            bottomTabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.material_white));
        else bottomTabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.material_black));
        //indicator color
        bottomTabLayout.setIndicatorColor(R.color.uou);
        //indicator line color

        context = getBaseContext();
        activity = this;

        button = findViewById(R.id.sortsong);

        mVisualizer = findViewById(R.id.blast);


    }

    /**
     * Show fragment in container
     * @param id Menu item res id
     */
    public void switchFragment(int id) {

        Fragment fragment = null;
        switch (id) {
            case R.id.menu_button1:
                fragment = fragment_music_list.newInstance(R.color.blue, "Music");
                break;
            case R.id.menu_button2:
                fragment = fragment_music_artist.newInstance("", "Artist");
                break;
            case R.id.menu_button3:
                fragment = fragment_plau_queue.newInstance("", "Queue");
                break;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(container, fragment);
            transaction.commit();
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    public interface OnBackPressedListener {
        void doBack();

       // boolean onCreateOptionsMenu(Menu menu);
    }

}
