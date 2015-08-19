package com.example.fahadtariq.ottawatraffic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.example.fahadtariq.ottawatraffic.HTTPClient.AsynchComplete;
import com.example.fahadtariq.ottawatraffic.HTTPClient.HTTPAsynchTask;
import com.example.fahadtariq.ottawatraffic.models.RingPost;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCyclingNetwork extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<RingPost> listRingPosts;
    private String apiURL = "http://data.ottawa.ca/dataset/5ae29db9-0299-40e8-8b99-3607fc0d4ba2/" +
            "resource/26808493-00ef-460c-be5b-019080ca5f68/download/cyclingnetworkshp.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_cycling_network);

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
        HTTPAsynchTask task_cycling_network = new HTTPAsynchTask(this, apiURL, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                int i=0;
                try {
                    //Tranform the string into a json object
                    final JSONObject json = new JSONObject(result);
                    JSONArray featureArray = json.getJSONArray("features");

                    //while(!featureArray.isNull(i))
                    while(i<200)
                    // This code is currently displaying 200 routes/shapes on map.
                    // To display all routes/shapes uncomment the above line of code
                    // and comment the next line. It takes a lot of time to display all routes on map (60+ minutes).
                    {
                        Log.d("RecordNumber", "i = " + i);
                        JSONObject feature = featureArray.getJSONObject(i);
                        JSONObject geometry = feature.getJSONObject("geometry");
                        JSONObject properties = feature.getJSONObject("properties");
                        String existingC = (String) properties.getString("EXISTING_C");
                        Double shapeLength = (Double) properties.getDouble("SHAPE_Leng");

                        int lineColor = Color.BLUE;

                        switch (existingC) {
                            case "Path":
                                lineColor = Color.BLUE;
                                break;
                            case "Bike Lane":
                                lineColor = Color.RED;
                                break;
                            case "Paved Shoulder":
                                lineColor = Color.MAGENTA;
                                break;
                            case "Segregated Bike Lane":
                                lineColor = Color.CYAN;
                                break;
                            case "Suggested Route":
                                lineColor = Color.BLACK;
                                break;
                        }

                        String geometryType= geometry.getString("type");
                        if(geometryType.compareTo("LineString")==0)//handling LineString shapes
                        {
                            JSONArray coordinatesArray = geometry.getJSONArray("coordinates");
                            JSONArray srcCoordinate = coordinatesArray.getJSONArray(0);
                            double srcLongitude = (double) srcCoordinate.getDouble(0);
                            double srcLatitude = (double) srcCoordinate.getDouble(1);

                            /*Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(srcLatitude, srcLongitude))
                            );// drawing marker for first point of a route/LineString.*/

                            for(int j=1;j<coordinatesArray.length();j++)
                            {
                                JSONArray destCoordinate = coordinatesArray.getJSONArray(j);
                                double destLongitude=(double) destCoordinate.getDouble(0);
                                double destLatitude=(double) destCoordinate.getDouble(1);

                                /*mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(srcLatitude, srcLongitude), 2000));
                                Marker marker2 = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(destLatitude,   destLongitude)));//drawing marker for each coordinate point on map.*/

                                PolylineOptions lineOptions = new PolylineOptions();
                                lineOptions.add(new LatLng(srcLatitude, srcLongitude), new LatLng(destLatitude, destLongitude));
                                lineOptions.color(lineColor);
                                lineOptions.width(8);
                                lineOptions.geodesic(true);

                                Polyline line = mMap.addPolyline(lineOptions);// drawing Polyline btw different points on map to display/draw shape/route

                                srcCoordinate=destCoordinate;
                                srcLongitude=destLongitude;
                                srcLatitude=destLatitude;
                            }
                        }
                        else
                        if(geometryType.compareTo("MultiLineString")==0)//handling MultiLineString
                        {
                            JSONArray pointArray = geometry.getJSONArray("coordinates");

                            for(int k=0;k<pointArray.length();k++)
                            {
                                JSONArray coordinatesArray= pointArray.getJSONArray(k);

                                JSONArray srcCoordinate = coordinatesArray.getJSONArray(0);
                                double srcLongitude=(double) srcCoordinate.getDouble(0);
                                double srcLatitude=(double) srcCoordinate.getDouble(1);

                                /*Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(srcLatitude, srcLongitude))
                                        .title("6900"));*/

                                for(int j=1;j<coordinatesArray.length();j++)
                                {
                                    JSONArray destCoordinate = coordinatesArray.getJSONArray(j);
                                    double destLongitude=(double) destCoordinate.getDouble(0);
                                    double destLatitude=(double) destCoordinate.getDouble(1);

                                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(srcLatitude, srcLongitude), 2000));
                                    /*Marker marker2 = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(destLatitude,   destLongitude)));*/

                                    PolylineOptions lineOptions = new PolylineOptions();
                                    lineOptions.add(new LatLng(srcLatitude, srcLongitude), new LatLng(destLatitude, destLongitude));
                                    lineOptions.color(lineColor);
                                    lineOptions.width(8);
                                    lineOptions.geodesic(true);

                                    Polyline line = mMap.addPolyline(lineOptions);

                                    srcCoordinate=destCoordinate;
                                    srcLongitude=destLongitude;
                                    srcLatitude=destLatitude;
                                }
                            }
                        }
                        i++;
                    }
//               }

                }
                catch (JSONException e) {
                    Log.e("JSON EXCEPTION", "INDEX  " + i);
                }
            }
        });
        task_cycling_network.execute();


        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(45.41914,-75.6974));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }



    // Function when the map legend button is clicked
    public void openLegendDialog(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Cycling Network Legend");
        alertDialog.setMessage(Html.fromHtml("Path = <font color='#0000ff'>Blue</font><br /> Bike Lane = <font color='#ff0000'>Red</font><br />" +
                "Paved Shoulder = <font color='#ff00ff'>Magenta</font><br /> Segregated Bike Lane = <font color='#00ffff'>Cyan</font>" +
                "<br /> Suggested Route = <font color='#000000'>Black</font><br />"));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
