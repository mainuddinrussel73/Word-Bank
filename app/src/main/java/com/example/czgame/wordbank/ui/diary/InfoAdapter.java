package com.example.czgame.wordbank.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import static com.example.czgame.wordbank.ui.diary.DiaryMain.arrayList;
import static com.example.czgame.wordbank.ui.diary.DiaryMain.db;

public class InfoAdapter extends ArrayAdapter<Info> {
    public static final int TYPE_ODD = 0;
    public static final int TYPE_EVEN = 1;
    Context context;
    ArrayList<Info> ItemList;
    int type;

    public InfoAdapter(@NonNull Context context, ArrayList<Info> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
       //System.out.println(ItemList.size());

        //int p = position%ItemList.size();
        if(position>=0) {
            if (ItemList.get(position % ItemList.size()).getDescription().trim().isEmpty() == true) {
                type = 1;
            } else {
                type = 0;
            }
        }
        return type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = null;
        int typeb = getItemViewType(position);

        if(view == null){//if view null then create new view

            if (typeb == TYPE_EVEN) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.voice_item, null);

            }
            if (typeb == TYPE_ODD) {
                view  = LayoutInflater.from(getContext()).inflate(R.layout.dairy_item, null);

            }

            //view= LayoutInflater.from(context).inflate(R.layout.dairy_item,parent,false);//for creating view
        }

        if(typeb == TYPE_EVEN){
            Info item = ItemList.get(position%ItemList.size());

            //finding listview shape component
            TextView subject = view.findViewById(R.id.subjectListViewShapeId);
            TextView date = view.findViewById(R.id.dateListViewShapeId);
            ImageView description = view.findViewById(R.id.descriptionListtViewShapeId);

            //ImageButton delete = view.findViewById(R.id.textsave);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(arrayList.get(position%ItemList.size()).getDescription().trim().isEmpty()==false){
                        Intent intent = new Intent(context,UpdateDiary.class);
                        intent.putExtra("subject",arrayList.get(position%ItemList.size()).getSubject());
                        intent.putExtra("description",arrayList.get(position%ItemList.size()).getDescription());
                        intent.putExtra("listId",arrayList.get(position%ItemList.size()).getId());
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context,UpdateDiary_voice.class);
                        intent.putExtra("subject",arrayList.get(position%ItemList.size()).getSubject());
                        intent.putExtra("medialink",arrayList.get(position%ItemList.size()).getMedialink());
                        intent.putExtra("listId",arrayList.get(position%ItemList.size()).getId());
                        context.startActivity(intent);
                    }
                }
            });


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    System.out.println("long click");

                    // TODO Auto-generated method stub
                    // System.out.println(position);
                    String pos = item.getId();
                    // Toast.makeText(getContext(),pos+"",Toast.LENGTH_SHORT).show();

                    Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    boolean isDark = prefs.getBoolean("isDark", false);
                    if (isDark) {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);

                    } else {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    }

                    PopupMenu popup = new PopupMenu(wrapper, view);
                    popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                    //
                    popup.show();


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Delete")){
                                db.delete((pos));
                                arrayList.remove((pos));
                                ItemList.remove(pos);
                                notifyDataSetInvalidated();
                                Intent intent = new Intent(context,DiaryMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //context.finish();
                                context.startActivity(intent);
                            }
                            return true;
                        }
                    });


                    return true;
                }
            });
            //return super.getView(position, convertView, parent);

            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            if (isDark) {

                //System.out.println("klklkl");
                // listitem.setBackgroundColor(Color.BLACK);



                subject.setTextColor(context.getResources().getColor(R.color.av_dark_blue));
                date.setTextColor(Color.RED);
                view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));

                Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_waver);

                myIcon2.setTint(Color.WHITE);
                description.setBackground(myIcon2);



            } else {

                subject.setTextColor(context.getResources().getColor(R.color.av_dark_blue));
                date.setTextColor(Color.RED);
                view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));

                Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_waver);

                myIcon2.setTint(Color.BLACK);
                description.setBackground(myIcon2);

            }

            //setting listview shape component to arrryList
            subject.setText(item.getSubject());
            date.setText(item.getDateTime());
        }
        if(typeb == TYPE_ODD){
            Info item = ItemList.get(position%ItemList.size());

            //finding listview shape component
            TextView subject = view.findViewById(R.id.subjectListViewShapeId);
            TextView date = view.findViewById(R.id.dateListViewShapeId);
            TextView description = view.findViewById(R.id.descriptionListtViewShapeId);
            //return super.getView(position, convertView, parent);

         //   ImageButton delete = view.findViewById(R.id.textsave);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(arrayList.get(position%ItemList.size()).getDescription().trim().isEmpty()==false){
                        Intent intent = new Intent(context,UpdateDiary.class);
                        intent.putExtra("subject",arrayList.get(position%ItemList.size()).getSubject());
                        intent.putExtra("description",arrayList.get(position%ItemList.size()).getDescription());
                        intent.putExtra("listId",arrayList.get(position%ItemList.size()).getId());
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context,UpdateDiary_voice.class);
                        intent.putExtra("subject",arrayList.get(position%ItemList.size()).getSubject());
                        intent.putExtra("medialink",arrayList.get(position%ItemList.size()).getMedialink());
                        intent.putExtra("listId",arrayList.get(position%ItemList.size()).getId());
                        context.startActivity(intent);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    System.out.println("long click");

                    // TODO Auto-generated method stub
                    // System.out.println(position);
                    String pos = item.getId();
                    // Toast.makeText(getContext(),pos+"",Toast.LENGTH_SHORT).show();

                    Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    boolean isDark = prefs.getBoolean("isDark", false);
                    if (isDark) {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);

                    } else {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    }

                    PopupMenu popup = new PopupMenu(wrapper, view);
                    popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                    //
                    popup.show();


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Delete")){
                                db.delete((pos));
                                arrayList.remove((pos));
                                ItemList.remove(pos);
                                Intent intent = new Intent(context,DiaryMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //context.finish();
                                context.startActivity(intent);
                            }
                            return true;
                        }
                    });


                    return true;
                }
            });

            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            if (isDark) {

                //System.out.println("klklkl");
                // listitem.setBackgroundColor(Color.BLACK);

                subject.setTextColor(context.getResources().getColor(R.color.av_dark_blue));
                date.setTextColor(Color.RED);
                description.setTextColor(ContextCompat.getColor(context,R.color.bg));
                view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));



            } else {

                subject.setTextColor(context.getResources().getColor(R.color.av_dark_blue));
                date.setTextColor(Color.RED);
                description.setTextColor(ContextCompat.getColor(context,R.color.bg));
                view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));

            }

            //setting listview shape component to arrryList
            subject.setText(item.getSubject());
            date.setText(item.getDateTime());
            description.setText(item.getDescription());
        }




        return view;
    }
}