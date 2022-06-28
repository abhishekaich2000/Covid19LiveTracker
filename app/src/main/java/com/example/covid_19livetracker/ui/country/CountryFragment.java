package com.example.covid_19livetracker.ui.country;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19livetracker.CountryDetails;
import com.example.covid_19livetracker.CovidAdapter;
import com.example.covid_19livetracker.R;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CountryFragment extends Fragment {
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    List<CountryDetails> countryDetails;
    CovidAdapter covidAdapter;
    private static final String TAG = "CountryFragment";
     AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_Counter);
        mProgressBar = root.findViewById(R.id.progressBar_Country);

        mAdView = root.findViewById(R.id.addView1);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MobileAds.initialize(getActivity(),"ca-app-pub-6754604932503864/4893790061");


        if(checkInternetConnection()) {
            getDataFromServer();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Internet Connection");
            builder.setMessage("Do you want to exit ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Exit", (dialog, which) -> {

                dialog.dismiss();
                Objects.requireNonNull(getActivity()).finish();
            }).setNegativeButton("Try Again", (dialog, which) -> {
                dialog.dismiss();
                Objects.requireNonNull(getActivity()).recreate();
            });
            builder.show();
        }

        return root;
    }

    private void getDataFromServer()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "https://corona.lmao.ninja/v3/covid-19/countries?sort=cases";
        countryDetails = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    JSONObject imgFetch = data.getJSONObject("countryInfo");

                    CountryDetails temp = new CountryDetails(data.getString("country"), data.getString("cases"), data.getString("recovered")
                            , data.getString("deaths"),imgFetch.getString("flag"));
                    countryDetails.add(temp);

                    Log.d(TAG, "onResponse: To string :" + temp.toString());
                }

                covidAdapter = new CovidAdapter(getContext(),countryDetails);
                mRecyclerView.setAdapter(covidAdapter);
                mProgressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            mProgressBar.setVisibility(View.GONE);
            error.printStackTrace();
        });
        Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(stringRequest);
    }

    private boolean checkInternetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }
}
