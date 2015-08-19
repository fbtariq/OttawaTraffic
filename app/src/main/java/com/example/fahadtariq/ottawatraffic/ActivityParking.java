package com.example.fahadtariq.ottawatraffic;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.fahadtariq.ottawatraffic.HTTPClient.AsynchComplete;
import com.example.fahadtariq.ottawatraffic.HTTPClient.HTTPAsynchTask;
import com.example.fahadtariq.ottawatraffic.Parse.ParseCSV;
import com.example.fahadtariq.ottawatraffic.Parse.ParseContext;
import com.example.fahadtariq.ottawatraffic.Parse.ParseJSON;
import com.example.fahadtariq.ottawatraffic.models.ParkRideLot;
import com.example.fahadtariq.ottawatraffic.models.ParkingLot;
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

import java.util.ArrayList;
import java.util.List;

public class ActivityParking extends FragmentActivity implements CompoundButton.OnCheckedChangeListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<ParkingLot> listParkingLots;
    private ArrayList<ParkRideLot> listParkRideLots;
    private String apiURL1 = "http://traffic.ottawa.ca/map/parking_list";
    private String apiURL2 = "http://traffic.ottawa.ca/map/park_and_ride_list";
    List<Marker> markersParkingLots = new ArrayList<Marker>();
    List<Marker> markersParkRideLots = new ArrayList<Marker>();
    CheckBox checkBox_ParkingLots;
    CheckBox checkBox_ParkRideLots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_parking);
        checkBox_ParkingLots = (CheckBox) findViewById(R.id.checkBox_ParkingLots);
        checkBox_ParkingLots.setOnCheckedChangeListener(this);
        checkBox_ParkRideLots = (CheckBox) findViewById(R.id.checkBox_ParkRideLots);
        checkBox_ParkRideLots.setOnCheckedChangeListener(this);

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
        // Setting a custom info window adapter for the google map so that none of the text is cut-off
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame. Nothing fancy
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);

                TextView tvTitle = ((TextView) v.findViewById(R.id.title));
                tvTitle.setText(marker.getTitle());
                TextView tvSnippet = ((TextView) v.findViewById(R.id.snippet));
                tvSnippet.setText(marker.getSnippet());

                return v;
            }
        });

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_parking_list = new HTTPAsynchTask(this, apiURL1, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL1.substring(apiURL1.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listParkingLots = (ArrayList<ParkingLot>)(ArrayList<?>) context.ParseString("ParkingLot",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_parking);

                for (ParkingLot data : listParkingLots) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .snippet(data.mapSnippet())
                            .icon(icon));
                    markersParkingLots.add(m);
                }
            }
        });
        task_parking_list.execute();

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_park_ride_list = new HTTPAsynchTask(this, apiURL2, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL2.substring(apiURL2.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listParkRideLots = (ArrayList<ParkRideLot>)(ArrayList<?>) context.ParseString("ParkRideLot",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_carpool);

                for (ParkRideLot data : listParkRideLots) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .snippet(data.mapSnippet())
                            .icon(icon));
                    markersParkRideLots.add(m);
                }
            }
        });
        task_park_ride_list.execute();

        /*
        The following code is used to detect the user's current location and automatically move the camera to that location.
        This code hasn't been added to the other activities since I am currently residing in Toronto and this code would
        cause me to have to zoom out and go to Ottawa manually. Only this activity has this code so that it can be showed
        in the project demo video.
         */
        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        // Check if user's location is available or not
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(13)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(45.41914,-75.6974));
            CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkBox_ParkingLots:
                for(Marker m : markersParkingLots) {
                    m.setVisible(isChecked);
                }
                break;
            case R.id.checkBox_ParkRideLots:
                for(Marker m : markersParkRideLots) {
                    m.setVisible(isChecked);
                }
                break;
        }
    }
}
