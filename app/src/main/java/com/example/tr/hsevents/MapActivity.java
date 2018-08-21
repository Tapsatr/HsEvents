package com.example.tr.hsevents;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback
{

    private static final String TAG = "";
    public float coords0;
    public float coords1;
    private GoogleMap mMap;

    public void onMapReady(GoogleMap map)
    {
        mMap = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String locUrl = intent.getStringExtra("locUrl");

        System.out.println(locUrl);

        new GetDataPlaces().execute(locUrl);

        SupportMapFragment fmap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        fmap.getMapAsync(this);
    }
    public class GetDataPlaces extends AsyncTask<String, Void, Void>
    {

        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(MapActivity.this,"Json Data is downloading"
                    ,Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urls)
        {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = urls[0];
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null)
            {
                try
                {
                    JSONObject obj;

                        // Getting JSON Array node
                        obj = new JSONObject(jsonStr);
                      //  JSONArray data = obj.getJSONArray("data");



                        //   Log.e(TAG, "Response from url: " + jsonStr);

                        // Getting JSON Array node

                        // looping through All Contacts
                     //   for (int i = 0; i < data.length(); i++)
                      //  {

                           // JSONObject n = data.getJSONObject(i);
                            JSONObject position = obj.getJSONObject("position");
                            // System.out.println("JSON OBJEKTINA"+position.getJSONObject("coordinates").toString());

                            JSONArray pos = position.getJSONArray("coordinates");
                            String scoords0 = pos.getString(0);
                            String scoords1 = pos.getString(1);
                    coords0 = Float.parseFloat(scoords0);
                    coords1 = Float.parseFloat(scoords1);



                       //}

                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coords1, coords0))
                    .title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coords1, coords0), 10));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            System.out.println("map ready:" +coords0);
            super.onPostExecute(result);
        }
    }
}
