package com.example.czgame.wordbank.ui.backup_scheudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PicassoImageGetter  implements Html.ImageGetter {
    Context c;
    View container;

    public PicassoImageGetter(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask =
                new ImageGetterAsyncTask( urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        Drawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call

            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0
                    + result.getIntrinsicHeight());


            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable = result;

            // redraw the image by invalidating the container
            PicassoImageGetter.this.container.invalidate();
        }


        public Drawable fetchDrawable(String urlString) {
            ConnectivityManager conMgr =  (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo != null) {
                if (netInfo.isConnected()) {
                    // Internet Available
                    InputStream is = null;
                    try {
                        is = fetch(urlString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Drawable drawable = Drawable.createFromStream(is, "src");
                    drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
                            + drawable.getIntrinsicHeight());
                    return drawable;
                }else {
                    //No internet
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    Uri uri = Uri.parse(urlString);
                    String path = uri.getLastPathSegment();
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images_cz/"+path);
                    System.out.println(bitmap.getByteCount());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);


                    Drawable drawable =  Drawable.createFromStream(bs, "src");

                    drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
                            + drawable.getIntrinsicHeight());

                    return drawable;
                }
            } else {
                //No internet
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                Uri uri = Uri.parse(urlString);
                String path = uri.getLastPathSegment();
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images_cz/"+path);
                System.out.println("no conn");
                System.out.println(bitmap.getByteCount());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                System.out.println(bs.toString());
                Drawable drawable =  Drawable.createFromStream(bs, "src");

                System.out.println(drawable.getIntrinsicHeight());
                System.out.println(drawable.getIntrinsicWidth());
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
                        + drawable.getIntrinsicHeight());

                return drawable;
            }

        }

        private InputStream fetch(String urlString) throws IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);

            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(response.getEntity());
            InputStream instream = bufHttpEntity.getContent();
            Bitmap bmp = BitmapFactory.decodeStream(instream);
            System.out.println("kk"+bmp.toString());

            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/images_cz";
            File dir = new File(file_path);

            System.out.println(file_path);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            // File file = new File(dir, urlString.substring(0,7) + ".png");
            Uri uri = Uri.parse(urlString);
            String path = uri.getLastPathSegment();
            File file = new File (dir, path);
            //if (file.exists ()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }




            return bufHttpEntity.getContent();
        }
    }
}
