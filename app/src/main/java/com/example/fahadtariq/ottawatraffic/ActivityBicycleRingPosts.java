package com.example.fahadtariq.ottawatraffic;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.fahadtariq.ottawatraffic.HTTPClient.AsynchComplete;
import com.example.fahadtariq.ottawatraffic.HTTPClient.HTTPAsynchTask;
import com.example.fahadtariq.ottawatraffic.Parse.ParseCSV;
import com.example.fahadtariq.ottawatraffic.Parse.ParseContext;
import com.example.fahadtariq.ottawatraffic.Parse.ParseJSON;
import com.example.fahadtariq.ottawatraffic.models.RingPost;
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

public class ActivityBicycleRingPosts extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<RingPost> listRingPosts;
    private String apiURL = "http://data.ottawa.ca/dataset/c4c196c5-62e5-4234-ae86-be14068f46f5/" +
            "resource/bb4588d1-257b-47be-9e16-23d56d3a12cb/download/ringpostmasterlist-feb122013.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_bicycle_rings_posts);

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
        HTTPAsynchTask task_bike_rings_posts_list = new HTTPAsynchTask(this, apiURL, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL.substring(apiURL.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listRingPosts = (ArrayList<RingPost>)(ArrayList<?>) context.ParseString("RingPost",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_bicycle_ring_posts);

                for (RingPost data : listRingPosts) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(data.getLatitude(), data.getLongitude()))
                                .title(data.mapTitle())
                                .snippet(data.mapSnippet())
                                .icon(icon));
                }
            }
        });
        task_bike_rings_posts_list.execute();


        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(45.41914,-75.6974));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }
}
