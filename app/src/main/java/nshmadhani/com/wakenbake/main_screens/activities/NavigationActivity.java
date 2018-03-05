package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

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

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = NavigationActivity.class.getSimpleName();
    private List<Places> mPlacesList;
    private RecyclerView mPlacesRecyclerView;
    private PlacesListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        ActionBar actionBar = getSupportActionBar(); // support.v7
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String apiKey = getString(R.string.google_api_key);

        mPlacesList = new ArrayList<>();
        mPlacesRecyclerView  = findViewById(R.id.recyclerView);
        mPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new PlacesListAdapter(mPlacesList, this);
        mPlacesRecyclerView.setAdapter(mListAdapter);
        mPlacesRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);

        Log.d(TAG, "onCreate: ");

        gettingPlacesFromGoogle(latitude, longitude);
        gettingPlacesFromFirebaseDatabase();

    }

    private void gettingPlacesFromFirebaseDatabase() {
        //Getting places from Firebase
        //Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApiInterface apiInterface = retrofit.create(RetrofitApiInterface.class);

        Call<List<Places>> call = apiInterface.getPlacesFromFirebase("search");

        Log.d(TAG, "onCreate: Connection successful");

        call.enqueue(new Callback<List<Places>>() {

            @Override
            public void onResponse(@NonNull Call<List<Places>> call, @NonNull Response<List<Places>> response) {
                List<Places> placesList = response.body();

                if (placesList != null) {
                    for(Places p : placesList) {
                        final Places places =  new Places();
                        places.setName(p.name);
                        Log.d(TAG, "onResult: "+p.getName());
                        mPlacesList.add(p);
                    }
                }
                Log.d(TAG, "onResult: "+mPlacesList.size());
                NavigationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Places>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void gettingPlacesFromGoogle(double latitude, double longitude) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        NearbySearchRequest nearbySearchRequest = new NearbySearchRequest(geoApiContext);

        nearbySearchRequest
                .location(new LatLng(latitude, longitude))
                .radius(1000)
                .type(PlaceType.RESTAURANT , PlaceType.CAFE ,PlaceType.FOOD ,PlaceType.BAKERY)
                .openNow(true)
                .rankby(RankBy.PROMINENCE)
                .setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        for (PlacesSearchResult place : result.results) {
                            Places places = new Places();
                            places.setName(place.name);
                            Log.d(TAG, "onResult: "+places.getName());
                            mPlacesList.add(places);
                        }
                        Log.d(TAG, "onResult: "+mPlacesList.size());
                        NavigationActivity.this.runOnUiThread(new Runnable() {
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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
