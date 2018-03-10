package nshmadhani.com.wakenbake.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.adapters.PlacesListAdapter;
import nshmadhani.com.wakenbake.interfaces.RetrofitApiInterface;
import nshmadhani.com.wakenbake.models.Places;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener ,
        MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {

    public static final String TAG = NavigationActivity.class.getSimpleName();
    private List<Places> mPlacesList;
    private RecyclerView mPlacesRecyclerView;
    private PlacesListAdapter mListAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new com.google.android.gms.maps.model.LatLng(-40, -168),
            new com.google.android.gms.maps.model.LatLng(71, 136));
    protected GeoDataClient mGeoDataClient;
    public String apiKey = "AIzaSyBiREqfd8QWqyeTii3djqE0IhVmKCfoHjs";
    public FirebaseAuth auth;
    public FirebaseUser user;
    public TextView navHeaderName;
    public TextView navHeaderEmail;
    public ImageView navHeaderImage;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
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

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        navHeaderName = findViewById(R.id.navHeaderNameTextView);
        navHeaderEmail = findViewById(R.id.navHeaderEmailTextView);
        navHeaderImage = findViewById(R.id.navHeaderImageView);

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search...");
        //searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setRoundedSearchBarEnabled(false);
        searchBar.setPlaceHolder("Search");
        searchBar.setOnSearchActionListener(this);
        //restore last queries from disk
//        if (lastSearches != null) {
//            lastSearches = loadSearchSuggestionFromDisk();
//        }
        //searchBar.setLastSuggestions(lastSearches);
        //Inflate menu and setup OnMenuItemClickListener
        //searchBar.inflateMenu(R.menu.search);
        //searchBar.getMenu().setOnMenuItemClickListener(this);

        Intent intent = getIntent();

//        navHeaderEmail.setText(user.getEmail());
//        navHeaderName.setText(user.getDisplayName());
//        try {
//            Picasso.with(this)
//                    .load(user.getPhotoUrl())
//                    .into(navHeaderImage);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Construct a GeoDataClient.
        mGeoDataClient = com.google.android.gms.location.places.Places.getGeoDataClient(this, null);

        mPlacesList = new ArrayList<>();
        mPlacesRecyclerView  = findViewById(R.id.recyclerView);
        mPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new PlacesListAdapter(mPlacesList, this);
        mPlacesRecyclerView.setAdapter(mListAdapter);
        mPlacesRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);

        Log.d(TAG, "onCreate: ");

        gettingPlacesFromGoogle(latitude, longitude);
        gettingPlacesFromFirebaseDatabase();
    }

//    private List<String> loadSearchSuggestionFromDisk() {
//    }

    private void gettingPlacesFromFirebaseDatabase() {
        //Getting places from Firebase
        final Gson gson;
        gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RetrofitApiInterface apiInterface = retrofit.create(RetrofitApiInterface.class);
        Call<String> call = apiInterface.getPlacesFromFirebase("dam");
        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                List<Places> placesList = response.body();
//                if (placesList != null) {
//                    for(Places p : placesList) {
//                        final Places places =  new Places();
//                        places.setName(p.name);
//                        Log.d(TAG, "onResult: "+p.getName());
//                        mPlacesList.add(p);
//                    }
//                }
//                Log.d(TAG, "onResult: "+mPlacesList.size());
                Log.d(TAG, "onResponse: "+response.body());
                NavigationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ",t);
            }
        });
    }

    private void gettingPlacesFromGoogle(final double latitude, final double longitude) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        NearbySearchRequest nearbySearchRequest = new NearbySearchRequest(geoApiContext);

        nearbySearchRequest
                .location(new LatLng(latitude, longitude))
                .type(PlaceType.RESTAURANT , PlaceType.CAFE ,PlaceType.FOOD ,PlaceType.BAKERY)
                //.openNow(true)
                .rankby(RankBy.DISTANCE)
                .setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        for (PlacesSearchResult place : result.results) {
                            Places places = new Places();
                            places.setName(place.name);
                            places.setLatitude(place.geometry.location.lat);
                            places.setLongitude(place.geometry.location.lng);
                            places.setPlaceId(place.placeId);
                            places.setRatings(place.rating);
                            Log.d(TAG, "onResult: " + place.placeId);
                            String URL = "";

                            try {
                                URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                        + place.photos[0].photoReference + "&key=" + apiKey;
                            } catch (Exception e) {
                                URL = "http://via.placeholder.com/350x150";
                            }
                            places.setImageUrl(URL);
                            Log.d(TAG, "onResult: "+places.getName() + places.getRatings());
//                            Log.d(TAG, "onResult: Reference " + places.getPhotoReference());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_home) {
            item.setChecked(false);
            intent = new Intent(getApplicationContext(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_bookmarks) {
            item.setChecked(false);
        } else if (id == R.id.nav_settings) {
            item.setChecked(false);
        } else if (id == R.id.nav_logout) {
            item.setChecked(false);

            auth.signOut();
            intent = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_share) {
            item.setChecked(false);

        } else if (id == R.id.nav_send) {
            item.setChecked(false);
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //save last queries to disk
        //saveSearchSuggestionToDisk(searchBar.getLastSuggestions());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Toast.makeText(NavigationActivity.this, "Search " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    void filter(String text) {
        List<Places> temp = new ArrayList<>();
        for (Places p : mPlacesList) {
                if (p.getName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(p);
            }
            mListAdapter.updateList(temp);
        }
    }
}
