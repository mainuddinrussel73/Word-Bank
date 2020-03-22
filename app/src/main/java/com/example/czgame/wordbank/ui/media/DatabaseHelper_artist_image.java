package com.example.czgame.wordbank.ui.media;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper_artist_image extends SQLiteOpenHelper {

    private static final int databaseVersion = 1;
    private static final String databaseName = "artistImage";
    private static final String TABLE_IMAGE = "ImageTable";
    // Image Table Columns names
    private static final String COL_ID = "col_id";
    private static final String IMAGE_ID = "image_id";
    private static final String IMAGE_BITMAP = "image_bitmap";
    private final String TAG = "DatabaseHelper_artist_imageClass";
    private Context context;

    public DatabaseHelper_artist_image(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + COL_ID + " INTEGER PRIMARY KEY ,"
                + IMAGE_ID + " TEXT,"
                + IMAGE_BITMAP + " TEXT )";
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(sqLiteDatabase);
    }

    public void insetImage(Bitmap dbDrawable, String imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_ID, imageId);
        Bitmap bitmap = (dbDrawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(IMAGE_BITMAP, stream.toByteArray());
        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public boolean Iscontains(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT * FROM ImageTable WHERE IMAGE_ID = ?; ", new String[]{id});

        if (cursor2.moveToFirst()) {
           // System.out.println("true");
            return  true;

        }

        cursor2.close();
        db.close();
        return false;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_IMAGE);
    }

    public ImageHelper getImage(String imageId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT * FROM ImageTable WHERE image_id = ?; ", new String[]{imageId});
        ImageHelper imageHelper = new ImageHelper();

        if (cursor2.moveToFirst()) {
            do {
                System.out.println(cursor2.getString(1));
               // System.out.println(cursor2.getString(2));
                imageHelper.setImageId(cursor2.getString(1));
                imageHelper.setImageByteArray(cursor2.getBlob(2));
             //   System.out.println(imageHelper.toString());
               // System.out.println();
            } while (cursor2.moveToNext());
        }

        cursor2.close();
        db.close();
        System.out.println(imageHelper.toString());
        return imageHelper;
    }
    public Cursor getImageAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.rawQuery("select * from " + TABLE_IMAGE, null);
        if (cursor2.moveToFirst()) {
            do {
                System.out.println(cursor2.getString(1));
//                System.out.println(cursor2.getString(2));
            } while (cursor2.moveToNext());
        }
        return cursor2;
    }

}
