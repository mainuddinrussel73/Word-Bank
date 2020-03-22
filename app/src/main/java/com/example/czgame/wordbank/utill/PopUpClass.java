package com.example.czgame.wordbank.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import androidx.annotation.NonNull;

public class  PopUpClass {

    //PopupWindow display method

    ViewGroup root;

    public  PopUpClass( ViewGroup r ){
        this.root = r;
    }

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public void showPopupWindow(Context context,final View view, String a,String b) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupdetail, null);

        //Specify the length and width through constants

        applyDim(root,0.5f);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;



        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        //Initialize the elements of our window, install the handler

        TextView test2 = popupView.findViewById(R.id.goldNamet);
        test2.setMovementMethod(new ScrollingMovementMethod());
        test2.setText(a);

        TextView test3 = popupView.findViewById(R.id.goldName);
        test3.setMovementMethod(new ScrollingMovementMethod());
        test3.setText(b);

        ImageButton buttonEdit = popupView.findViewById(R.id.messageButton);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                clearDim(root);
                popupWindow.dismiss();

            }
        });


        LinearLayout relativeLayout = popupView.findViewById(R.id.nnmnm);
        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if(isDark){
            relativeLayout.setBackgroundColor(Color.BLACK);
            test2.setTextColor(Color.WHITE);
            test3.setTextColor(Color.WHITE);
        }else{
            relativeLayout.setBackgroundColor(Color.WHITE);
            test2.setTextColor(Color.BLACK);
            test3.setTextColor(Color.BLACK);
        }


        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                //popupWindow.dismiss();

                return true;
            }
        });
    }

}