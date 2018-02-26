package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.adapters.PlacesListAdapter;
import nshmadhani.com.wakenbake.main_screens.interfaces.RetrofitApiInterface;
import nshmadhani.com.wakenbake.main_screens.models.Places;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private List<Places> mPlacesList;
    private RecyclerView mPlacesRecyclerView;
    private PlacesListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlacesList = new ArrayList<>();
        mPlacesRecyclerView  = findViewById(R.id.recyclerView);
        mPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new PlacesListAdapter(mPlacesList);
        mPlacesRecyclerView.setAdapter(mListAdapter);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);

        Log.d(TAG, "onCreate: ");

        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        NearbySearchRequest nearbySearchRequest = new NearbySearchRequest(geoApiContext);

        nearbySearchRequest
                    .location(new LatLng(latitude, longitude))
                    .radius(1000)
                    .type(PlaceType.FOOD)
                    .openNow(true)
                    .setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                        @Override
                        public void onResult(PlacesSearchResponse result) {

                            for (PlacesSearchResult place : result.results) {
                                Places places =  new Places();

                                places.setName(place.name);
                                Log.d(TAG, "onResult: "+places.getName());
                                mPlacesList.add(places);
                            }
                            Log.d(TAG, "onResult: "+mPlacesList.size());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mListAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        @Override
                        public void onFailure(Throwable e) {
                            Log.e(TAG, "onFailure: ", e);
                        }
                    });

        //Getting places from Firebase
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitApiInterface apiInterface = retrofit.create(RetrofitApiInterface.class);

        Call<List<Places>> call = apiInterface.getPlacesFromFirebase();

        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<List<Places>>() {

            @Override
            public void onResponse(@NonNull Call<List<Places>> call, @NonNull Response<List<Places>> response) {
                List<Places> placesList = response.body();

                if (placesList != null) {
                    for(Places p : placesList) {
                        Places placesFromFirebase =  new Places();
                        placesFromFirebase.setName(p.name);
                        Log.d(TAG, "onResult: "+p.getName());
                        mPlacesList.add(p);
                    }
                }
                Log.d(TAG, "onResult: "+mPlacesList.size());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Places>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
