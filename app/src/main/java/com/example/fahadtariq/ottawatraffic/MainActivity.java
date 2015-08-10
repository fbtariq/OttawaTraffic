package com.example.fahadtariq.ottawatraffic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    private static final String TAG = "TrafficActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void open_brp(View view) {
        Intent intent = new Intent(this, ActivityBicycleRingPosts.class);
        startActivity(intent);
    }

    public void open_tc(View view) {
        Intent intent = new Intent(this, ActivityCameras.class);
        startActivity(intent);
    }

    public void open_bcn(View view) {
        Intent intent = new Intent(this, ActivityCyclingNetwork.class);
        startActivity(intent);
    }

    public void open_tp(View view) {
        Intent intent = new Intent(this, ActivityParking.class);
        startActivity(intent);
    }

    public void open_ti(View view) {
        Intent intent = new Intent(this, ActivityTrafficIncidents.class);
        startActivity(intent);
    }
}
