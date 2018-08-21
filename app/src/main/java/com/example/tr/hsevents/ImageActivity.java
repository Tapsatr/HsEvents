package com.example.tr.hsevents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import java.io.InputStream;
import java.net.URL;


public class ImageActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra("imageUrl");

        new DownLoadImageTask((ImageView) findViewById(R.id.imageView)).execute(imageUrl);
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>
    {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView)
        {
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls)
        {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try
            {
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }
            catch(Exception e)
            { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result)
        {
            imageView.setImageBitmap(result);
        }
    }
}
