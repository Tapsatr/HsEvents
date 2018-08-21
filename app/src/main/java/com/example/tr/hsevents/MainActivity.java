package com.example.tr.hsevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{

    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<DataModel> dataModels;
    ArrayList<KeyWords> keyWords;
    ArrayList<KeyWords> places;
    ListView listView;
    private static CustomAdapter adapter;
    private static KeyWordCustomAdapter adapterKey;
    private static KeyWordCustomAdapter adapterPlace;
    private static String keywordId;
    private static String PlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText editText = (EditText) findViewById(R.id.editTextStart);
        EditText editText1 = (EditText) findViewById(R.id.editTextEnd);

        new DateText(editText);
        new DateText(editText1);

        new GetDataKeyWords().execute("http://api.hel.fi/linkedevents/v0.1/keyword/?format=json");
        new GetDataPlaces().execute();


        listView = (ListView) findViewById(R.id.list);


        dataModels = new ArrayList<>();


        adapter = new CustomAdapter(dataModels, MainActivity.this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                DataModel dataModel = dataModels.get(position);

                Snackbar.make(view, dataModel.getName() + "\n" + dataModel.getPlace() + " PVM: " + dataModel.getStartDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
        keyWords = new ArrayList<>();
        adapterKey = new KeyWordCustomAdapter(keyWords, MainActivity.this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_search);
        // Creating ArrayAdapter using the string array and default spinner layout
        // Specify layout to be used when list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner.setAdapter(adapterKey);
        //spinner.setOnItemSelectedListener(this);


        places = new ArrayList<>();
        adapterPlace = new KeyWordCustomAdapter(places, MainActivity.this);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_searchPlace);
        // Creating ArrayAdapter using the string array and default spinner layout
        // Specify layout to be used when list of choices appears
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner2.setAdapter(adapterPlace);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                KeyWords keyword = adapterKey.getItem(position);

                System.out.println(keyword.getId());
                keywordId = keyword.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                KeyWords keyword = adapterPlace.getItem(position);

                System.out.println(keyword.getId());
                PlaceId = keyword.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    public void Search(View view) throws ParseException
    {
        String startDate;
        String endDate;

        Spinner spinner = (Spinner) findViewById(R.id.spinner_search);
        EditText editTextStart = (EditText) findViewById(R.id.editTextStart);
        EditText editTextEnd = (EditText) findViewById(R.id.editTextEnd);


        if (spinner != null && spinner.getSelectedItem() != null)
        {
            adapter.clear();
            adapter.notifyDataSetChanged();

            //   String keyword = spinner.getSelectedItem().toString();

            // String place = spinner2.getSelectedItem().toString();

            //muutetaan pvm
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String start = editTextStart.getText().toString();
            String end = editTextEnd.getText().toString();


            if (!start.equals("") || !end.equals(""))
            {


                Date newEndDate = format.parse(end);
                Date newDate = format.parse(start);


                format = new SimpleDateFormat("yyyy-MM-dd");
                startDate = format.format(newDate);
                endDate = format.format(newEndDate);
            } else
            {
                startDate = "";
                endDate = "";
            }


            if (PlaceId == null || PlaceId.equals(""))
            {
                new GetDataKeyWords().execute("http://api.hel.fi/linkedevents/v0.1/event/?end=" + endDate + "&format=json&start=" + startDate + "&keyword=" + keywordId + "");
            } else
            {
                new GetDataKeyWords().execute("http://api.hel.fi/linkedevents/v0.1/event/?end=" + endDate + "&format=json&start=" + startDate + "&keyword=" + keywordId + "&location=" + PlaceId);
            }
        } else
        {
            Toast.makeText(MainActivity.this, "One keyword is required!"
                    , Toast.LENGTH_LONG).show();
        }
    }

    private class GetDataKeyWords extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading"
                    , Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urls)
        {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = urls[0];

            String jsonStr = sh.makeServiceCall(url);

            String str1 = "http://api.hel.fi/linkedevents/v0.1/keyword/?format=json";
            String str2 = "http://api.hel.fi/linkedevents/v0.1/event/";
            if (jsonStr != null)
            {
                try
                {
                    JSONObject n;
                    JSONObject obj;
                    JSONObject meta;
                    JSONObject name;
                    JSONObject loc;
                    JSONObject info;
                    String fi = "";
                    String sId;
                    String location = "";
                    String startTime = "";
                    String endTime = "";
                    String finfo = "";
                    String locUrl = "";
                    String imageUrl = "";

                    do
                    {
                        // Getting JSON Array node
                        obj = new JSONObject(jsonStr);
                        JSONArray data = obj.getJSONArray("data");
                        System.out.println(url);

                        Log.e(TAG, "Response from url: " + jsonStr);

                        // looping through All Contacts

                        if (url.toLowerCase().contains(str1.toLowerCase())) // keywordit
                        {
                            for (int i = 0; i < data.length(); i++)
                            {

                                n = data.getJSONObject(i);
                                name = n.getJSONObject("name");
                                fi = name.getString("fi");
                                sId = n.getString("id");


                                keyWords.add(new KeyWords(fi, sId));
                                //adapterKey.notifyDataSetChanged();

                            }
                        } else if (url.toLowerCase().contains(str2.toLowerCase())) // etsitään eventti
                        {
                            for (int i = 0; i < data.length(); i++)
                            {
                                n = data.getJSONObject(i);
                                try
                                {
                                    name = n.getJSONObject("name");

                                    startTime = n.getString("start_time");
                                    startTime = startTime.replace('Z', ' ');
                                    startTime = startTime.replaceAll("T", " KLO: ");

                                    endTime = n.getString("end_time");
                                    endTime = endTime.replace('Z', ' ');
                                    endTime = endTime.replaceAll("T", " KLO: ");

                                    info = n.getJSONObject("description");

                                    imageUrl = n.getString("image");
                                    System.out.println(imageUrl);


                                    finfo = info.getString("fi");

                                    locUrl = n.getJSONObject("location").getString("@id");


                                    fi = name.getString("fi");
                                    loc = n.getJSONObject("location_extra_info");
                                    if (!loc.getString("fi").equals("null"))
                                    {
                                        location = loc.getString("fi");
                                    }

                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                                dataModels.add(new DataModel(fi, location, startTime, finfo, endTime, locUrl, imageUrl));
                                //adapterKey.notifyDataSetChanged();

                            }
                        }

                        meta = obj.getJSONObject("meta");
                        url = meta.getString("next");

                        jsonStr = sh.makeServiceCall(url);

                    } while (!meta.getString("next").equals("null"));

                } catch (final JSONException e)
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

            } else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
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
            adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, FeaturesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class GetDataPlaces extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading"
                    , Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://api.hel.fi/linkedevents/v0.1/place/?format=json";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null)
            {
                try
                {
                    JSONObject obj;
                    JSONObject meta;
                    String fi;
                    String sId;
                    do
                    {
                        // Getting JSON Array node
                        obj = new JSONObject(jsonStr);
                        JSONArray data = obj.getJSONArray("data");

                        meta = obj.getJSONObject("meta");

                        url = meta.getString("next");
                        jsonStr = sh.makeServiceCall(url);

                        //   Log.e(TAG, "Response from url: " + jsonStr);

                        // Getting JSON Array node

                        // looping through all data
                        for (int i = 0; i < data.length(); i++)
                        {

                            JSONObject n = data.getJSONObject(i);
                            sId = n.getString("id");
                            JSONObject name = n.getJSONObject("name");
                            fi = name.getString("fi");

                            // adding contact to contact list
                            places.add(new KeyWords(fi, sId));
                            //adapterKey.notifyDataSetChanged();
                        }

                    } while (!meta.getString("next").equals("null"));
                } catch (final JSONException e)
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

            } else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
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
            super.onPostExecute(result);
        }
    }
}
