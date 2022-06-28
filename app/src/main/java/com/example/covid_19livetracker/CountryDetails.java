package com.example.covid_19livetracker;

public class CountryDetails
{
    String mCountry,mCases,mRecovered,mDeaths,mPhotoUrl;

    public CountryDetails(String country, String cases, String recovered, String deaths,String photoUrl)
    {
        mCountry = country;
        mCases = cases;
        mRecovered = recovered;
        mDeaths = deaths;
        mPhotoUrl = photoUrl;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCases() {
        return mCases;
    }

    public String getRecovered() {
        return mRecovered;
    }

    public String getDeaths() {
        return mDeaths;
    }
    public  String getPhotoUrl()
    {
        return mPhotoUrl;
    }

    @Override
    public String toString() {
        return "CountryDetails{" +
                "mCountry='" + mCountry + '\'' +
                ", mCases='" + mCases + '\'' +
                ", mRecovered='" + mRecovered + '\'' +
                ", mDeaths='" + mDeaths + '\'' +
                '}';
    }
}
