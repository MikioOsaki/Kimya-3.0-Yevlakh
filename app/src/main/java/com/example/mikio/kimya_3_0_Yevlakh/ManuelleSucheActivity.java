package com.example.mikio.kimya_3_0_Yevlakh;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ManuelleSucheActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private AdapterFish mAdapter;

    SearchView searchView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuelle_suche);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) ManuelleSucheActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ManuelleSucheActivity.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();

        }
    }

// Create class AsyncFetch
private class AsyncFetch extends AsyncTask<String, String, String> {

    ProgressDialog pdLoading = new ProgressDialog(ManuelleSucheActivity.this);
    HttpURLConnection conn;
    URL url = null;
    String searchQuery;

    public AsyncFetch(String searchQuery){
        this.searchQuery=searchQuery;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

    }

    @Override
    protected String doInBackground(String... params) {
        try {

            // Enter URL address where your php file resides
            url = new URL("http://141.45.92.216/fish-search.php");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput to true as we send and recieve data
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // add parameter to our above url
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                return (result.toString());

            } else {
                return("Connection error");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    @Override
    protected void onPostExecute(String result) {

        //this method will be running on UI thread
        pdLoading.dismiss();
        List<DataFish> data=new ArrayList<>();

        pdLoading.dismiss();
        if(result.equals("no rows")) {
            Toast.makeText(ManuelleSucheActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
        }else{

            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonData = jArray.getJSONObject(i);
                    String fishName = jsonData.getString("fish_name");
                    String catName = jsonData.getString("cat_name");
                    String sizeName = jsonData.getString("size_name");
                    int price = jsonData.getInt("price");
                    String url = jsonData.getString("url");

                    DataFish fishData = new DataFish(
                            fishName,
                            catName,
                            sizeName,
                            price,
                            "http://www.gigatronik.com/fileadmin/Pdf/Beispiel.pdf");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFish = (RecyclerView) findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(ManuelleSucheActivity.this, data);
                mRVFish.setAdapter(mAdapter);
                mRVFish.setLayoutManager(new LinearLayoutManager(ManuelleSucheActivity.this));

            } catch (JSONException e) {
                // You to understand what actually error is and handle it appropriately
                Toast.makeText(ManuelleSucheActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(ManuelleSucheActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}
}