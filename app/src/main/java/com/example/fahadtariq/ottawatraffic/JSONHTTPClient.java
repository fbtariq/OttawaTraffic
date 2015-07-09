package com.example.fahadtariq.ottawatraffic;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to get the JSON string from a URL
 */
public class JSONHTTPClient {

    public String getJSONstring(String url) {
        HttpURLConnection urlConnection  = null;
        //InputStream stream = null;

        try {
            urlConnection  = (HttpURLConnection) ( new URL(url)).openConnection();
            //connection.setRequestMethod("GET");
            //connection.setDoInput(true);
            //connection.setDoOutput(true);
            urlConnection .connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            //stream = urlConnection .getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection .getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            //stream.close();
            urlConnection.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            urlConnection .disconnect();
        }

        return null;
    }
}
