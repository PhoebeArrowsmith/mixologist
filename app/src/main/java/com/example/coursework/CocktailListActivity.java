package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
This class constructs the CocktailListActivity screen using the activity_cocktail_list.xml file.
It implements the recycler view listing the cocktails.
The list of cocktails is retrieved via an asynchronous call to an API.
 */
public class CocktailListActivity extends AppCompatActivity{

    //Limits the number of rows returned to 100
    private static final int NUM_LIST_ITEMS = 100;

    //Declares the variables
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    //Initialise tag for logging
    private static final String TAG = "CocktailListActivity";

    //Create the second activity and load all data from savedInstanceState
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log message for onCreate
        Log.i(TAG, "onCreate");

        //Restore any saved instance state
        super.onCreate(savedInstanceState);

        //Set the content view
        setContentView(R.layout.activity_cocktail_list);

        //Retrieving the name of the spirit selected in the main activity
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        String spirit = extras.getString("spirit");

        //Set the title bar
        this.setTitle(spirit + " Cocktails for You!!");

        //Setting the URL of the API
        URL url = null;
        try {
            url = new URL("https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=" + spirit);
        } catch (MalformedURLException e) {
            //Log message for error when the URL isn't valid
            Log.e(TAG, "URL not Valid");
        }

        //Create object of ApiTask class and execute it
        new ApiTask().execute(url);
    }

    //Asynchronous task to retrieve data via API
    public class ApiTask extends AsyncTask<URL, String, String > {
        //Override the doInBackground method to execute API
        @Override
        protected String doInBackground(URL... params) {
            URL apiUrl = params[0];
            String ApiResult = null;
            try {
                ApiResult = getResponseFromHttpUrl(apiUrl);
            } catch (IOException e) {
                //Log message for error when connecting to the internet
                Log.e(TAG, "Failed to Connect to the Internet");
                //Insert 2 rows into the array to display the error message in the Recycler View
                ApiResult = "{drinks:[{\"idDrink\": \"0\", \"strDrink\": \"Issue connecting to internet\"}, {\"idDrink\": \"0\", \"strDrink\": \"Please check internet connections\" }]}";
            }
            return ApiResult;
        }

        //Not currently called
        @Override
        protected void onProgressUpdate(String... progress) {
            String errMsg = progress[0];
            Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
        }

        //Override onPostExecute to retrieve data from the JSON object returned by the API
        @Override
        protected void onPostExecute(String ApiResult) {
            String[] cocktails = new String[0];
            String[] cocktailIds = new String[0];

            if (ApiResult != null && !ApiResult.equals("")) {

                //Get JSON Array inside the returned JSON Object
                JSONArray jArray = null;
                try {
                    JSONObject jObject = new JSONObject(ApiResult);
                    jArray = jObject.getJSONArray("drinks");
                    //Get list of cocktail names from the JSON Array
                    cocktails = new String[jArray.length()];
                    cocktailIds = new String[jArray.length()];
                    for(int i=0; i<cocktails.length; i++) {
                        cocktails[i] = jArray.getJSONObject(i).getString("strDrink");
                        cocktailIds[i] = jArray.getJSONObject(i).getString("idDrink");
                    }
                } catch (JSONException e) {
                    //Log message for error when retrieving API results
                    Log.e(TAG, "Getting JSON Object from API Results");
                }
            }else{
                //In case the API fails
                cocktails[0] = "Failed to get data via API";
                cocktailIds[0] = "0";
                cocktails[1] = " ";
                cocktailIds[1] = "0";
            }

            //Get the Recycler View
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            //Giving the Recycler View a layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(CocktailListActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);

            //Creating an adapter and supplying the cocktail String Array
            mRecyclerViewAdapter = new RecyclerViewAdapter(cocktails, cocktailIds, CocktailListActivity.this);

            //Divider for the recycler view
            mRecyclerView.addItemDecoration(new DividerItemDecoration(CocktailListActivity.this, DividerItemDecoration.VERTICAL));
            Drawable mDivider = ContextCompat.getDrawable(CocktailListActivity.this, R.drawable.divider);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(CocktailListActivity.this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(mDivider);

            //Connecting the adapter to the RecyclerView
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }
    }

    //Storing the response from the API
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        //Log message for getResponseFromHttpUrl
        Log.i(TAG, "getResponseFromHttpUrl");

        //Opening the HTTP connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    //Creates an options options_menu_activity_main using the options_menu_activity_cocktail_list.xml file
    public boolean onCreateOptionsMenu(Menu menu2){
        //Log message for onCreateOptionsMenu
        Log.i(TAG, "onCreateOptionsMenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_activity_cocktail_list, menu2);
        return true;
    }

    //Deals with the options_menu_activity_main item selected
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log message for onOptionsItemSelected
        Log.i(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.go_home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
