package com.example.czgame.wordbank.ui.alertme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import androidx.core.content.ContextCompat;


class AlarmListAdapter extends BaseAdapter
{
    private final String TAG = "AlarmMe";

    private Context mContext;
    private DataSource mDataSource;
    private LayoutInflater mInflater;
    private DateTime mDateTime;
    private int mColorOutdated;
    private int mColorActive;
    private AlarmManager mAlarmManager;

    public AlarmListAdapter(Context context)
    {
        mContext = context;
        mDataSource = DataSource.getInstance(context);

        Log.i(TAG, "AlarmListAdapter.create()");

        mInflater = LayoutInflater.from(context);
        mDateTime = new DateTime(context);

        mColorOutdated = mContext.getResources().getColor(R.color.av_dark_blue);
        mColorActive = mContext.getResources().getColor(R.color.av_dark_blue);

        mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        dataSetChanged();
    }

    public void save()
    {
        DataSource.save();
    }

    public void update(Alarm alarm)
    {
        DataSource.update(alarm);
        dataSetChanged();
    }

    public void updateAlarms()
    {
        Log.i(TAG, "AlarmListAdapter.updateAlarms()");
        for (int i = 0; i < DataSource.size(); i++)
            DataSource.update(DataSource.get(i));
        dataSetChanged();
    }

    public void add(Alarm alarm)
    {
        DataSource.add(alarm);
        dataSetChanged();
    }

    public void delete(int index)
    {
        cancelAlarm(DataSource.get(index));
        DataSource.remove(index);
        dataSetChanged();
    }

    public void onSettingsUpdated()
    {
        mDateTime.update();
        dataSetChanged();
    }

    public int getCount()
    {
        return DataSource.size();
    }

    public Alarm getItem(int position)
    {
        return DataSource.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Alarm alarm = DataSource.get(position);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_alarm_item, null);

            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.item_title);
            holder.details = convertView.findViewById(R.id.item_details);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(alarm.getTitle());
        holder.details.setText(mDateTime.formatDetails(alarm) + (alarm.getEnabled() ? "" : " [disabled]"));

        if (alarm.getOutdated())
            holder.title.setTextColor(mColorOutdated);
        else
            holder.title.setTextColor(mColorActive);


        SharedPreferences prefs = mContext.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);



            holder.details.setTextColor(Color.RED);
            convertView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.card_background_dark));



        } else {

            holder.details.setTextColor(Color.RED);
            convertView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.card_background));

        }
        return convertView;
    }

    private void dataSetChanged()
    {
        for (int i = 0; i < DataSource.size(); i++)
            setAlarm(DataSource.get(i));

        notifyDataSetChanged();
    }

    private void setAlarm(Alarm alarm)
    {
        PendingIntent sender;
        Intent intent;

        if (alarm.getEnabled() && !alarm.getOutdated())
        {
            intent = new Intent(mContext, AlarmReceiver.class);
            alarm.toIntent(intent);
            sender = PendingIntent.getBroadcast(mContext, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getDate(), sender);
            Log.i(TAG, "AlarmListAdapter.setAlarm(" + alarm.getId() + ", '" + alarm.getTitle() + "', " + alarm.getDate()+")");
        }
    }

    private void cancelAlarm(Alarm alarm)
    {
        PendingIntent sender;
        Intent intent;

        intent = new Intent(mContext, AlarmReceiver.class);
        sender = PendingIntent.getBroadcast(mContext, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(sender);
    }

    static class ViewHolder
    {
        TextView title;
        TextView details;
    }
}
