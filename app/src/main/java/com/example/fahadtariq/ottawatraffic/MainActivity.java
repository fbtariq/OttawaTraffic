package com.example.fahadtariq.ottawatraffic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
        //    .execute("http://traffic.ottawa.ca/opendata/camera?c=16&certificate=effilc5102nroht80607401&id=222");

        /*Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri1 = Uri.parse("geo:0,0?q=http://data.ottawa.ca/dataset/5ae29db9-0299-40e8-8b99-3607fc0d4ba2/resource/0f474163-a8ca-4deb-ace2-97a008028a68/download/cyclingnetwork.kmz");
        mapIntent.setData(uri1);
        mapIntent.putExtra("com.google.earth.EXTRA.tour_feature_id", "ID_00001");
        startActivity(mapIntent);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void open_gmaps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
