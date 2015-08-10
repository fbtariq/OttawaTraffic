package com.example.fahadtariq.ottawatraffic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadtariq.ottawatraffic.HTTPClient.AsynchComplete;
import com.example.fahadtariq.ottawatraffic.HTTPClient.HTTPAsynchTask;
import com.example.fahadtariq.ottawatraffic.Parse.ParseCSV;
import com.example.fahadtariq.ottawatraffic.Parse.ParseContext;
import com.example.fahadtariq.ottawatraffic.Parse.ParseJSON;
import com.example.fahadtariq.ottawatraffic.models.Camera;
import com.example.fahadtariq.ottawatraffic.models.RedLightCamera;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityCameras extends FragmentActivity implements CompoundButton.OnCheckedChangeListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<RedLightCamera> listRLCamera;
    private ArrayList<Camera> listCamera;
    private String apiURL1 = "http://traffic.ottawa.ca/map/red_light_camera_list";
    private String apiURL2 = "http://traffic.ottawa.ca/map/camera_list";
    List<Marker> markersRLCamera = new ArrayList<Marker>();
    List<Marker> markersCamera = new ArrayList<Marker>();
    CheckBox checkBox_Camera;
    CheckBox checkBox_RLCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_cameras);
        checkBox_Camera = (CheckBox) findViewById(R.id.checkBox_Camera);
        checkBox_Camera.setOnCheckedChangeListener(this);
        checkBox_RLCamera = (CheckBox) findViewById(R.id.checkBox_RLCamera);
        checkBox_RLCamera.setOnCheckedChangeListener(this);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_red_light_camera_list = new HTTPAsynchTask(this, apiURL1, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL1.substring(apiURL1.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listRLCamera = (ArrayList<RedLightCamera>)(ArrayList<?>) context.ParseString("RedLightCamera",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_red_camera);

                for (RedLightCamera data : listRLCamera) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .icon(icon));
                    markersRLCamera.add(m);
                }
            }
        });
        task_red_light_camera_list.execute();

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_camera_list = new HTTPAsynchTask(this, apiURL2, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL2.substring(apiURL2.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listCamera = (ArrayList<Camera>)(ArrayList<?>) context.ParseString("Camera",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_green_camera);

                for (Camera data : listCamera) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title("WW-" + data.getNumber())
                            .snippet(data.mapTitle())
                            .icon(icon));
                    markersCamera.add(m);
                }
            }
        });
        task_camera_list.execute();


        mMap.setTrafficEnabled(true);
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(45.41914,-75.6974));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String cameraNumber = marker.getTitle().substring(3);
        String isTrafficCamera =  marker.getTitle().substring(0,2);

        if (isTrafficCamera.equalsIgnoreCase("WW")) {

            String imageURL = "http://traffic.ottawa.ca/opendata/camera?c=" + cameraNumber + "&certificate=effilc5102nroht80607401&id=" + this.getUniqueUserID();

            View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastLinearLayout));

            new DownloadImageTask((ImageView) toastView.findViewById(R.id.toastImage))
                    .execute(imageURL);

            TextView textView = (TextView)toastView.findViewById(R.id.toastText);
            textView.setText(marker.getSnippet());

            Toast toast = new Toast(this);

            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastView);

            toast.show();

            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkBox_Camera:
                for(Marker m : markersCamera) {
                    m.setVisible(isChecked);
                }
                break;
            case R.id.checkBox_RLCamera:
                for(Marker m : markersRLCamera) {
                    m.setVisible(isChecked);
                }
                break;
        }
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

    public String getUniqueUserID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return (deviceUuid.toString()).replaceAll("-", "").trim();
    }
}
