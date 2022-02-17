package com.daclink.drew.sp22.cst438_project01_starter.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.daclink.drew.sp22.cst438_project01_starter.apis.SearchService;
import com.daclink.drew.sp22.cst438_project01_starter.models.IndividualSearch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String SEARCH_SERVICE_BASE_URL = "https://omdbapi.com/";

    private SearchService mSearchService;
    private MutableLiveData<IndividualSearch> mResponseLiveData;

    public MovieRepository() {
        mResponseLiveData = new MutableLiveData<IndividualSearch>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        mSearchService = new retrofit2.Retrofit.Builder()
                .baseUrl(SEARCH_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchService.class);
    }

    public void searchMovieByIMDB_Id(String imdbId) {
        mSearchService.searchValuesByIMDB_Id(imdbId)
                .enqueue(new Callback<IndividualSearch>() {
                    @Override
                    public void onResponse(Call<IndividualSearch> call, Response<IndividualSearch> response) {
                        if (response.body() != null) {
                            mResponseLiveData.postValue(response.body());
                            System.out.println(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<IndividualSearch> call, Throwable t) {
                        mResponseLiveData.postValue(null);
                    }
                });
    }

    public LiveData<IndividualSearch> getResponseLiveData() {
        return mResponseLiveData;
    }
}