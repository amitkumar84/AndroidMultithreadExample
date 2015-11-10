package com.example.sahamit.androidmultithreadexample;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Home extends AppCompatActivity {

    ImageView img,img1,img2,img3,img4;
    ProgressBar progress,progress1,progress2,progress3,progress4;
    Bitmap bitmap=null;
    String url ="http://dummyimage.com/600x400/c78050/fff&text=Amit+Kumar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        img = (ImageView)findViewById(R.id.image);
        img1=(ImageView)findViewById(R.id.image1);
        img2=(ImageView)findViewById(R.id.image2);
        img3=(ImageView)findViewById(R.id.image3);
        img4 = (ImageView)findViewById(R.id.image4);
        progress=(ProgressBar)findViewById(R.id.progress);
        progress1=(ProgressBar)findViewById(R.id.progress1);
        progress2=(ProgressBar)findViewById(R.id.progress2);
        progress3=(ProgressBar)findViewById(R.id.progress3);
        progress4=(ProgressBar)findViewById(R.id.progress4);

        new Thread(new Runnable() {
             public void run() {
                final Bitmap bitmap = loadImageFromNetwork(url);
                img.post(new Runnable() {
                    public void run() {
                        img.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                final Bitmap bitmap = loadImageFromNetwork(url);
                img1.postDelayed(new Runnable() {
                    public void run() {
                        img1.setImageResource(View.VISIBLE);
                        progress1.setVisibility(View.GONE);
                        img1.setImageBitmap(bitmap);
                    }
                }, 100);
            }
        }).start();

        new LoadImageFromUrl().execute(url);

        new Thread(new Runnable() {
            public void run() {
                final Bitmap bitmap = loadImageFromNetwork(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress3.setVisibility(View.GONE);
                        img3.setVisibility(View.VISIBLE);
                        img3.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                bitmap = loadImageFromNetwork(url);
                messageHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Bitmap loadImageFromNetwork(String src) {
         try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private class LoadImageFromUrl extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap=null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                progress2.setVisibility(View.GONE);
                img2.setVisibility(View.VISIBLE);
                img2.setImageBitmap(image);

            }else{
                Toast.makeText(Home.this, "Image Url corrupted or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress4.setVisibility(View.GONE);
            img4.setVisibility(View.VISIBLE);
            img4.setImageBitmap(bitmap);
        }
    };

}
