package com.example.covid_19livetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    private TextView totCase,totRecovered,totDeaths,totActive,todayCases,todayDeaths,casePerM,deathPerM,testPerM,totTest;
    private TextView countryName;
    private ImageView countryImg;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        totCase = findViewById(R.id.totCases);
        totRecovered = findViewById(R.id.totRecovered);
        totDeaths = findViewById(R.id.totDeath);
        totActive = findViewById(R.id.totActive);
        todayCases = findViewById(R.id.todaysCases);
        todayDeaths = findViewById(R.id.todaysDeaths);
        casePerM = findViewById(R.id.casesPerM);
        deathPerM = findViewById(R.id.deathPerM);
        testPerM = findViewById(R.id.testsPerM);
        totTest = findViewById(R.id.totTests);

        countryName = findViewById(R.id.countryNameDet);
        countryImg = findViewById(R.id.countyFlag);

        mProgressBar = findViewById(R.id.progress_bar_detailsactivity);

        String name = getIntent().getStringExtra("country");
        String url = "https://corona.lmao.ninja/v3/covid-19/countries/" + name;

        if(checkInternet()) {
            gatherDetails(url);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("No Internet Connection");
            builder.setMessage("Do you want to exit ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Exit", (dialog, which) -> {

                dialog.dismiss();
                finish();
            }).setNegativeButton("Try Again", (dialog, which) -> {
                dialog.dismiss();
                recreate();
            });
            builder.show();
        }
    }

    private void gatherDetails(String url)
    {
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

            try {

                mProgressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(response);

                countryName.setText(jsonObject.getString("country"));
                totCase.setText(jsonObject.getString("cases"));
                totRecovered.setText(jsonObject.getString("recovered"));
                totDeaths.setText(jsonObject.getString("deaths"));
                totActive.setText(jsonObject.getString("active"));
                todayCases.setText(jsonObject.getString("todayCases"));
                todayDeaths.setText(jsonObject.getString("todayDeaths"));
                casePerM.setText(jsonObject.getString("casesPerOneMillion"));
                deathPerM.setText(jsonObject.getString("deathsPerOneMillion"));
                totTest.setText(jsonObject.getString("tests"));
                testPerM.setText(jsonObject.getString("testsPerOneMillion"));

                JSONObject fetchImg = jsonObject.getJSONObject("countryInfo");

                Picasso.get().load(fetchImg.getString("flag")).fit().centerCrop().into(countryImg);

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, error -> {
            mProgressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error :" + error.getMessage(),Toast.LENGTH_LONG).show();
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
    private boolean checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }
}
