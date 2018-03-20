package nshmadhani.com.wakenbake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener ,
        MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {

    public static final String TAG = NavigationActivity.class.getSimpleName();
    protected GeoDataClient mGeoDataClient;
    public FirebaseAuth mAuth;
    private MaterialSearchBar searchBar;
    public DrawerLayout drawer;
    public ViewPager mNavigationViewPager;
    public TextView mHeaderEmail;
    public ImageView mHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        initialLayout();
        mAuth = FirebaseAuth.getInstance();
        relatedToSearch();

        mHeaderEmail = findViewById(R.id.navHeaderEmail);
        mHeaderImage = findViewById(R.id.navHeaderImage);

        // Construct a GeoDataClient.
        mGeoDataClient = com.google.android.gms.location.places.Places.getGeoDataClient(this, null);

        mNavigationViewPager = findViewById(R.id.nav_view_pager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Night", FirebasePlacesFragment.class)
                .add("Day", GooglePlacesFragment.class)
                .add("Tiffin", TiffinPlaceFragment.class)
                .create());

        mNavigationViewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.navviewpagertab);
        viewPagerTab.setViewPager(mNavigationViewPager);
    }

    private void relatedToSearch() {
        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Search...");
        searchBar.setRoundedSearchBarEnabled(false);
        searchBar.setPlaceHolder("Search");
        searchBar.setOnSearchActionListener(this);
    }

    private void initialLayout() {
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        Log.d(TAG, String.format("onCreate: %s", mAuth.getCurrentUser().getEmail()));
        if (user != null) {
//            Log.d(TAG, "onStart: " + user.getEmail() + user.getPhotoUrl().toString());
//                String email = user.getEmail();
//                mHeaderEmail.setText(email);
//                Picasso.with(this)
//                        .load(user.getPhotoUrl())
//                        .into(mHeaderImage);
        }
        Log.d(TAG, "onStart: ");
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            intent = new Intent(getApplicationContext(), BookmarkActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            item.setChecked(false);

        } else if (id == R.id.nav_settings) {

            item.setChecked(false);

        } else if (id == R.id.nav_logout) {

            item.setChecked(false);

            if (mAuth != null) {
                mAuth.signOut();
                intent = new Intent(NavigationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please Log In!", Toast.LENGTH_SHORT).show();
            }
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

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
                filter(charSequence.toString());
                Toast.makeText(NavigationActivity.this, "before text changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
                Toast.makeText(NavigationActivity.this, "on text changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
                Toast.makeText(NavigationActivity.this, "after text changed", Toast.LENGTH_SHORT).show();
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
//        List<GooglePlaces> temp = new ArrayList<>();
//        for (GooglePlaces p : mGooglePlacesList) {
//                if (p.getName().toLowerCase().contains(text.toLowerCase())) {
//                    temp.add(p);
//            }
//            mListAdapter.updateList(temp);
//        }
    }
}
