package com.example.fahadtariq.ottawatraffic;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.fahadtariq.ottawatraffic.HTTPClient.AsynchComplete;
import com.example.fahadtariq.ottawatraffic.HTTPClient.HTTPAsynchTask;
import com.example.fahadtariq.ottawatraffic.Parse.ParseCSV;
import com.example.fahadtariq.ottawatraffic.Parse.ParseContext;
import com.example.fahadtariq.ottawatraffic.Parse.ParseJSON;
import com.example.fahadtariq.ottawatraffic.models.Camera;
import com.example.fahadtariq.ottawatraffic.models.RedLightCamera;
import com.example.fahadtariq.ottawatraffic.models.TrafficConstruction;
import com.example.fahadtariq.ottawatraffic.models.TrafficIncident;
import com.example.fahadtariq.ottawatraffic.models.TrafficSpecialEvent;
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

public class ActivityTrafficIncidents extends FragmentActivity implements CompoundButton.OnCheckedChangeListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<TrafficConstruction> listConstruction;
    private ArrayList<TrafficIncident> listIncidents;
    private ArrayList<TrafficSpecialEvent> listSpecialEvents;
    private String apiURL1 = "http://traffic.ottawa.ca/map/construction_list";
    private String apiURL2 = "http://traffic.ottawa.ca/map/incident_list";
    private String apiURL3 = "http://traffic.ottawa.ca/map/special_event_list";
    List<Marker> markersConstruction = new ArrayList<Marker>();
    List<Marker> markersIncidents = new ArrayList<Marker>();
    List<Marker> markersSpecialEvents = new ArrayList<Marker>();
    CheckBox checkBox_Construction;
    CheckBox checkBox_Incidents;
    CheckBox checkBox_SpecialEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_traffic_incidents);
        checkBox_Construction = (CheckBox) findViewById(R.id.checkBox_Construction);
        checkBox_Construction.setOnCheckedChangeListener(this);
        checkBox_Incidents = (CheckBox) findViewById(R.id.checkBox_Incidents);
        checkBox_Incidents.setOnCheckedChangeListener(this);
        checkBox_SpecialEvents = (CheckBox) findViewById(R.id.checkBox_SpecialEvents);
        checkBox_SpecialEvents.setOnCheckedChangeListener(this);

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
        HTTPAsynchTask task_construction_list = new HTTPAsynchTask(this, apiURL1, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL1.substring(apiURL1.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listConstruction = (ArrayList<TrafficConstruction>)(ArrayList<?>) context.ParseString("TrafficConstruction",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_construction);

                for (TrafficConstruction data : listConstruction) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .snippet(data.mapSnippet())
                            .icon(icon));
                    markersConstruction.add(m);
                }
            }
        });
        task_construction_list.execute();

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_incident_list = new HTTPAsynchTask(this, apiURL2, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL2.substring(apiURL2.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listIncidents = (ArrayList<TrafficIncident>)(ArrayList<?>) context.ParseString("TrafficIncident",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_incidents);

                for (TrafficIncident data : listIncidents) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .snippet(data.mapSnippet())
                            .icon(icon));
                    markersIncidents.add(m);
                }
            }
        });
        task_incident_list.execute();

        // Initial JSON parse to get the latest URL for CSV resource
        HTTPAsynchTask task_special_event_list = new HTTPAsynchTask(this, apiURL3, new AsynchComplete() {
            @Override
            public void onAsynchTaskCompletion(String result) {
                ParseContext context;
                String apiURLExtension = apiURL3.substring(apiURL3.lastIndexOf("."));
                if (apiURLExtension.equalsIgnoreCase(".csv")) {
                    context = new ParseContext(new ParseCSV());
                } else {
                    context = new ParseContext(new ParseJSON());
                }
                listSpecialEvents = (ArrayList<TrafficSpecialEvent>)(ArrayList<?>) context.ParseString("TrafficSpecialEvent",result);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_special_events);

                for (TrafficSpecialEvent data : listSpecialEvents) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title(data.mapTitle())
                            .snippet(data.mapSnippet())
                            .icon(icon));
                    markersSpecialEvents.add(m);
                }
            }
        });
        task_special_event_list.execute();


        mMap.setTrafficEnabled(true);
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(45.41914,-75.6974));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkBox_Construction:
                for(Marker m : markersConstruction) {
                    m.setVisible(isChecked);
                }
                break;
            case R.id.checkBox_Incidents:
                for(Marker m : markersIncidents) {
                    m.setVisible(isChecked);
                }
                break;
            case R.id.checkBox_SpecialEvents:
                for(Marker m : markersSpecialEvents) {
                    m.setVisible(isChecked);
                }
                break;
        }
    }
}
