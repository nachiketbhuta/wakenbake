package nshmadhani.com.wakenbake.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Fragments.FirebasePlacesFragment;
import nshmadhani.com.wakenbake.Fragments.GooglePlacesFragment;
import nshmadhani.com.wakenbake.Fragments.TiffinPlaceFragment;
import nshmadhani.com.wakenbake.Models.FirebasePlaces;
import nshmadhani.com.wakenbake.Models.GooglePlaces;
import nshmadhani.com.wakenbake.Models.MasterData;
import nshmadhani.com.wakenbake.Models.TiffinPlaces;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener ,
        MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {
    public static GooglePlacesFragment fragG;
    public static FirebasePlacesFragment fragF;
    public static TiffinPlaceFragment fragT;
    public static final String TAG = NavigationActivity.class.getSimpleName();
    protected GeoDataClient mGeoDataClient;
    public FirebaseAuth mAuth;
    private MaterialSearchBar searchBar;
    public DrawerLayout drawer;
    public ViewPager mNavigationViewPager;
    public TextView mNavHeaderEmail;
    public ImageView mNavHeaderImage;
    public static MasterData mMaster;
    public static boolean isSearched=false;
    SmartTabLayout viewPagerTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
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
        View header = navigationView.getHeaderView(0);
        mNavHeaderEmail = header.findViewById(R.id.mNavHeaderEmail);
        mNavHeaderEmail.setText(mAuth.getCurrentUser().getEmail());

        mNavHeaderImage = header.findViewById(R.id.mNavHeaderImage);

        relatedToSearch();

        // Construct a GeoDataClient.
        mGeoDataClient = com.google.android.gms.location.places.Places.getGeoDataClient(this, null);

        mNavigationViewPager = findViewById(R.id.nav_view_pager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Night", FirebasePlacesFragment.class)
                .add("Day", GooglePlacesFragment.class)
                .add("Tiffin", TiffinPlaceFragment.class)
                .create()); //Adding the fragments to the SmartTabLayout

        mNavigationViewPager.setAdapter(adapter);

        viewPagerTab = findViewById(R.id.navviewpagertab);
        viewPagerTab.setViewPager(mNavigationViewPager);
    }

    private void relatedToSearch() {
        //Initializing the search bar
        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Search...");
        searchBar.setRoundedSearchBarEnabled(false);
        searchBar.setPlaceHolder("Search");
        searchBar.setOnSearchActionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @Override
    public void onBackPressed() {
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
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

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
        //Checking the state of the search bar
        String s = enabled ? "enabled" : "disabled";
        isSearched = false;
    }


    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isSearched = true;
                filter(charSequence.toString(), mNavigationViewPager.getCurrentItem());
                if (charSequence.toString().length() > 0) {

                    //Searching is done as per tabs
                    switch (mNavigationViewPager.getCurrentItem()) {
                        case 0 : fragF.setAdapter();
                            break;
                        case 1 : fragG.setAdapter();
                            break;
                        case 2 : fragT.setAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isSearched = true;
                filter(charSequence.toString(), mNavigationViewPager.getCurrentItem());

                if (charSequence.toString().length() > 0) {
                    //Searching is done as per tabs
                    switch (mNavigationViewPager.getCurrentItem()) {
                        case 0 : fragF.setAdapter();
                            break;
                        case 1 : fragG.setAdapter();
                            break;
                        case 2 : fragT.setAdapter();
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isSearched=true;

                filter(editable.toString(),mNavigationViewPager.getCurrentItem());
                if (editable.length() > 0) {
                    //Searching is done as per tabs
                    switch (mNavigationViewPager.getCurrentItem()) {
                        case 0 : fragF.setAdapter();
                            break;
                        case 1 : fragG.setAdapter();
                            break;
                        case 2 : fragT.setAdapter();
                            break;
                    }
                }
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

    void filter(String text,int TYPE) {

        //Creating temporary ArrayLists
        List<FirebasePlaces> tempF=new ArrayList<>();
        List<GooglePlaces> tempG=new ArrayList<>();
        List<TiffinPlaces> tempT=new ArrayList<>();
        mMaster=new MasterData();

        if (text != null) {

            switch(TYPE) {
                case 0:
                    for (FirebasePlaces f : FirebasePlacesFragment.mFirebasePlacesList) {
                        if (f.getmVendorName().toLowerCase().contains(text.toLowerCase())) {
                            //If the name is found, then it will be added to the temporary list
                            tempF.add(f);
                        }
                    }
                    mMaster.setNight(tempF);
                    break;
                case 1:
                    for (GooglePlaces g : GooglePlacesFragment.mGooglePlacesList) {
                        if (g.getName().toLowerCase().contains(text.toLowerCase())) {
                            //If the name is found, then it will be added to the temporary list
                            tempG.add(g);
                        }
                    }
                    mMaster.setDay(tempG);
                    break;
                case 2:
                    for (TiffinPlaces t : TiffinPlaceFragment.mTiffinPlacesList) {
                        if (t.getmTiffinName().toLowerCase().contains(text.toLowerCase())) {
                            //If the name is found, then it will be added to the temporary list
                            tempT.add(t);
                        }
                    }
                    mMaster.setTiffin(tempT);
                    break;
            }

        } else {
            Toast.makeText(this, "Please enter query!", Toast.LENGTH_SHORT).show();
        }
    }
}
