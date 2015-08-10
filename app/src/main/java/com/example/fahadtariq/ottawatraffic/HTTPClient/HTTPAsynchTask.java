package com.example.fahadtariq.ottawatraffic.HTTPClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class HTTPAsynchTask extends AsyncTask<Void,Void,String> {

    private Context context; // context reference
    private AsynchComplete callback;
    private ProgressDialog pDialog;
    private String url;

    public HTTPAsynchTask(Context context, String inURL, AsynchComplete callback){ //constructor
        this.context = context;
        this.url = inURL;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        pDialog = ProgressDialog.show(context, "Retrieving  Data...", "Please wait...", true);
        pDialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        JSONHTTPClient jParser = new JSONHTTPClient();
        String json = jParser.getJSONFromUrl(url);
        return json;
    }
    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);
        pDialog.hide();
        callback.onAsynchTaskCompletion(result);

    }
}