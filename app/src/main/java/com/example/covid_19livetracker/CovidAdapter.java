package com.example.covid_19livetracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CovidAdapter extends RecyclerView.Adapter<CovidAdapter.ViewHolder>{

    Context mContext;
    List<CountryDetails> mCountryDetails;
    List<CountryDetails> mFilteredCountry;

    public CovidAdapter(Context context, List<CountryDetails> countryDetails)
    {
        mContext = context;
        mCountryDetails = countryDetails;
        mFilteredCountry = countryDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.countrylayout_file,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CountryDetails flag = mCountryDetails.get(position);
        holder.countryName.setText(flag.getCountry());
        holder.totCases.setText(flag.getCases());
        holder.totRecovered.setText(flag.getRecovered());
        holder.totDeaths.setText(flag.getDeaths());
        Picasso.get().load(flag.getPhotoUrl()).fit().centerCrop().into(holder.photoView);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("country",flag.getCountry());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCountryDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView countryName,totCases,totRecovered,totDeaths;
        ImageView photoView;
        CardView mCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.countryName);
            totCases = itemView.findViewById(R.id.totalCasesRv);
            totRecovered = itemView.findViewById(R.id.totalRecoveredRv);
            totDeaths = itemView.findViewById(R.id.totalDeathsRv);
            photoView = itemView.findViewById(R.id.photo);
            mCardView = itemView.findViewById(R.id.cardView);
        }
    }

}
