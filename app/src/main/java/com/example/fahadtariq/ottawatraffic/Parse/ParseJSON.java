package com.example.fahadtariq.ottawatraffic.Parse;

import com.example.fahadtariq.ottawatraffic.models.Camera;
import com.example.fahadtariq.ottawatraffic.models.Model;
import com.example.fahadtariq.ottawatraffic.models.ParkRideLot;
import com.example.fahadtariq.ottawatraffic.models.ParkingLot;
import com.example.fahadtariq.ottawatraffic.models.RedLightCamera;
import com.example.fahadtariq.ottawatraffic.models.TrafficConstruction;
import com.example.fahadtariq.ottawatraffic.models.TrafficIncident;
import com.example.fahadtariq.ottawatraffic.models.TrafficSpecialEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseJSON implements StrategyParse {

    @Override
    public ArrayList<Model> Parse(String parseWhichList, String stringToParse) {

        ArrayList<Model> list = new ArrayList<Model>();

        int i=0;

        //Transform the string into a json array/object
        final JSONArray json;

        try {
            json = new JSONArray(stringToParse);

            switch (parseWhichList) {
                case "ParkingLot":
                    while(!json.isNull(i))
                    {
                        ParkingLot pl = new ParkingLot();

                        JSONObject record = json.getJSONObject(i);

                        pl.setId(((int) record.getInt("id")));
                        pl.setMessage(((String) record.getString("message")));
                        pl.setMessageFrench(((String) record.getString("messageFrench")));
                        pl.setDescription(((String) record.getString("description")));
                        pl.setLatitude((double) record.getDouble("latitude"));
                        pl.setLongitude((double) record.getDouble("longitude"));

                        list.add(pl);

                        i++;
                    }

                case "ParkRideLot":
                    while(!json.isNull(i))
                    {
                        ParkRideLot prl = new ParkRideLot();
                        JSONObject record = json.getJSONObject(i);

                        prl.setId(((int) record.getInt("id")));
                        prl.setMessage(((String) record.getString("message")));
                        prl.setMessageFrench(((String) record.getString("messageFrench")));
                        prl.setDescription(((String) record.getString("description")));
                        prl.setLatitude((double) record.getDouble("latitude"));
                        prl.setLongitude((double) record.getDouble("longitude"));

                        list.add(prl);

                        i++;
                    }


                case "RedLightCamera":
                    while(!json.isNull(i))
                    {
                        RedLightCamera rlc = new RedLightCamera();
                        JSONObject record = json.getJSONObject(i);

                        rlc.setId(((int) record.getInt("id")));
                        rlc.setMessage(((String) record.getString("message")));
                        rlc.setMessageFrench(((String) record.getString("messageFrench")));
                        rlc.setDescription(((String) record.getString("description")));
                        rlc.setDescriptionFrench(((String) record.getString("descriptionFrench")));
                        rlc.setLatitude((double) record.getDouble("latitude"));
                        rlc.setLongitude((double) record.getDouble("longitude"));

                        list.add(rlc);

                        i++;
                    }

                case "Camera":
                    while(!json.isNull(i))
                    {
                        Camera c = new Camera();
                        JSONObject record = json.getJSONObject(i);

                        c.setId(((int) record.getInt("id")));
                        c.setNumber(((int) record.getInt("number")));
                        c.setType(((String) record.getString("type")));
                        c.setDescription(((String) record.getString("description")));
                        c.setDescriptionFrench(((String) record.getString("descriptionFr")));
                        c.setLatitude((double) record.getDouble("latitude"));
                        c.setLongitude((double) record.getDouble("longitude"));

                        list.add(c);

                        i++;
                    }

                case "TrafficConstruction":
                    while(!json.isNull(i))
                    {
                        TrafficConstruction tc = new TrafficConstruction();
                        JSONObject record = json.getJSONObject(i);

                        tc.setId(((int) record.getInt("id")));
                        tc.setMessage(((String) record.getString("message")));
                        tc.setMessageFrench(((String) record.getString("messageFrench")));
                        tc.setDescription(((String) record.getString("description")));
                        tc.setDescriptionFrench(((String) record.getString("descriptionFrench")));
                        tc.setLatitude((double) record.getDouble("latitude"));
                        tc.setLongitude((double) record.getDouble("longitude"));

                        list.add(tc);

                        i++;
                    }

                case "TrafficIncident":
                    while(!json.isNull(i))
                    {
                        TrafficIncident ti = new TrafficIncident();
                        JSONObject record = json.getJSONObject(i);

                        ti.setId(((int) record.getInt("id")));
                        ti.setMessage(((String) record.getString("message")));
                        ti.setMessageFrench(((String) record.getString("messageFrench")));
                        ti.setDescription(((String) record.getString("description")));
                        ti.setDescriptionFrench(((String) record.getString("descriptionFrench")));
                        ti.setLatitude((double) record.getDouble("latitude"));
                        ti.setLongitude((double) record.getDouble("longitude"));

                        list.add(ti);

                        i++;
                    }

                case "TrafficSpecialEvent":
                    while(!json.isNull(i))
                    {
                        TrafficSpecialEvent tse = new TrafficSpecialEvent();
                        JSONObject record = json.getJSONObject(i);

                        tse.setId(((int) record.getInt("id")));
                        tse.setMessage(((String) record.getString("message")));
                        tse.setMessageFrench(((String) record.getString("messageFrench")));
                        tse.setDescription(((String) record.getString("description")));
                        tse.setDescriptionFrench(((String) record.getString("descriptionFrench")));
                        tse.setLatitude((double) record.getDouble("latitude"));
                        tse.setLongitude((double) record.getDouble("longitude"));

                        list.add(tse);

                        i++;
                    }

                case "CyclingNetwork":
                    while(!json.isNull(i))
                    {
                        TrafficSpecialEvent tse = new TrafficSpecialEvent();
                        JSONObject record = json.getJSONObject(i);

                        tse.setId(((int) record.getInt("id")));
                        tse.setMessage(((String) record.getString("message")));
                        tse.setMessageFrench(((String) record.getString("messageFrench")));
                        tse.setDescription(((String) record.getString("description")));
                        tse.setDescriptionFrench(((String) record.getString("descriptionFrench")));
                        tse.setLatitude((double) record.getDouble("latitude"));
                        tse.setLongitude((double) record.getDouble("longitude"));

                        list.add(tse);

                        i++;
                    }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
