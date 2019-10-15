package com.example.mainuddin.myapplication34.ui.promotodo;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class proadapter extends BaseAdapter {

    private Activity context;
    CustomViewHolder cvh;
    private List<promotododata> promotododataList;

    public proadapter(List<promotododata> promotododataList,Activity context) {
        this.promotododataList = new ArrayList<>();
        this.promotododataList.addAll(promotododataList);
        this.context = context;
    }

    @Override
    public int getCount() {
        return promotododataList.size();
    }

    @Override
    public Object getItem(int position) {
        return promotododataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return promotododataList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {



        if (rowView == null) {
            LayoutInflater inflater=context.getLayoutInflater();
            cvh = new CustomViewHolder();
            rowView = inflater.inflate(R.layout.item_custom, null,true);
            cvh.imgLogo = (CheckBox) rowView.findViewById(R.id.img_item_edit);
            cvh.txtName = (TextView) rowView.findViewById(R.id.txt_item_edit);
            cvh.imgLogo2 = (ImageView) rowView.findViewById(R.id.img_item_edit2);
            cvh.circularProgressBar = rowView.findViewById(R.id.circularProgressBar);
            cvh.circularProgressBar.setProgressMax(300);
            cvh.circularProgressBar1 = rowView.findViewById(R.id.circularProgressBar1);
            cvh.circularProgressBar1.setProgressMax(300);
            cvh.circularProgressBar2 = rowView.findViewById(R.id.circularProgressBar2);
            cvh.circularProgressBar2.setProgressMax(300);
            cvh.circularProgressBar3 = rowView.findViewById(R.id.circularProgressBar3);
            cvh.circularProgressBar3.setProgressMax(300);
            cvh.imgLogo2.setOnTouchListener(mOnTouchListener);
            cvh.imgLogo.setOnClickListener(mOnTouchListener1);
            rowView.setTag(cvh);
        } else {
            cvh = (CustomViewHolder) rowView.getTag();
        }
        promotododata item = (promotododata) this.getItem(position);
        cvh.txtName.setText(item.getTitle());
       // cvh.imgLogo.setImageDrawable(Utils.getDrawable(context, R.drawable.ic_timer_white_24dp));
        cvh.imgLogo2.setImageDrawable(Utils.getDrawable(context, R.drawable.ic_keyboard_arrow_left_black_24dp));
        cvh.imgLogo2.setTag(Integer.parseInt(Integer.toString(position)));
        cvh.imgLogo.setTag(Integer.parseInt(Integer.toString(position) ));
        cvh.circularProgressBar.setProgressBarColor(Color.RED);
        cvh.circularProgressBar1.setProgressBarColor(Color.RED);
        cvh.circularProgressBar2.setProgressBarColor(Color.RED);
        cvh.circularProgressBar3.setProgressBarColor(Color.RED);
        if(promotododataList.get(position).getNum_of_promotodo()==1){
            cvh.circularProgressBar.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
            cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
            cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
        }else if(promotododataList.get(position).getNum_of_promotodo()==2){
            cvh.circularProgressBar.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
            cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
        }else if(promotododataList.get(position).getNum_of_promotodo()==3){
            cvh.circularProgressBar.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
        }else if(promotododataList.get(position).getNum_of_promotodo()==4){
            cvh.circularProgressBar.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar1.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar2.setProgressWithAnimation(300, (long) 1000);
            cvh.circularProgressBar3.setProgressWithAnimation(300, (long) 1000);
        }else {

        }



        return rowView;
    }


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Object o = v.getTag();
            if (o != null && o instanceof Integer) {
                Promotodo_activity.mListView.startDrag(((Integer) o).intValue());
            }
            return false;
        }
    };
    class CustomViewHolder {
        public CheckBox imgLogo;
        public TextView txtName;
        public ImageView imgLogo2;
        public CircularProgressBar circularProgressBar;
        public CircularProgressBar circularProgressBar1;
        public CircularProgressBar circularProgressBar2;
        public CircularProgressBar circularProgressBar3;

    }

    private View.OnClickListener mOnTouchListener1  = new View.OnClickListener(){


        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            if (o != null && o instanceof Integer) {
                if ( ((CheckBox)v).isChecked() ) {

                    DBproHandle mDBHelper = new DBproHandle(context);
                    promotododata item = (promotododata)proadapter.this.getItem(((Integer) o).intValue());
                    //mDBHelper = new DBproHandle(Promotodo_activity.this);
                    int i = mDBHelper.deleteData(item.getTitle());
                    if(i==1){
                        Toasty.success(v.getContext(),"Task Completed!",Toasty.LENGTH_LONG).show();
                        //wait 1 second
                        ((CheckBox)v).postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                ((CheckBox)v).toggle();
                            }
                        }, 1000);
                        promotododataList.remove(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount());
                        proadapter.this.notifyDataSetChanged();
                    }else {
                        Toasty.error(v.getContext(),"Task not Completed!",Toasty.LENGTH_LONG).show();
                    }
                }
            }

        }


    };

}





