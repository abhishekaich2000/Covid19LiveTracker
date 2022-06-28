package com.example.covid_19livetracker.ui.home;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19livetracker.R;

import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private TextView totalCases,totalRecovered,totalDeaths;
    private ProgressBar mProgressBar;
    private static final String TAG = "HomeFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        totalCases = root.findViewById(R.id.totalCases);
        totalRecovered = root.findViewById(R.id.totalRecovered);
        totalDeaths = root.findViewById(R.id.totalDeaths);
        mProgressBar = root.findViewById(R.id.progressBar_Home);


        if(checkInternet()){
             getData();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("No internet connection");
            dialog.setMessage("Do you want to exit ?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Exit", (dialog1, which) -> {

                dialog1.dismiss();
                Objects.requireNonNull(getActivity()).finish();
            }).setNegativeButton("Try Again", (dialog12, which) -> {
                dialog12.dismiss();
                Objects.requireNonNull(getActivity()).recreate();
            });
            dialog.show();
        }

        return root;
    }

    private void getData()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String url = "https://corona.lmao.ninja/v2/all?yesterday=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            mProgressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                totalCases.setText(jsonObject.getString("cases"));
                totalRecovered.setText(jsonObject.getString("recovered"));
                totalDeaths.setText(jsonObject.getString("deaths"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            mProgressBar.setVisibility(View.GONE);
            Log.d(TAG, "onErrorResponse: Error is :" + error.getMessage());
        });
        queue.add(request);
    }

    private boolean checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }
}
