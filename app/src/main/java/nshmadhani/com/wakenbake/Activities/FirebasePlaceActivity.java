package nshmadhani.com.wakenbake.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nshmadhani.com.wakenbake.Fragments.ReviewFragment;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.Models.APIClient;
import nshmadhani.com.wakenbake.Models.PlaceBookmark;
import nshmadhani.com.wakenbake.Holders.RatingsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebasePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = FirebasePlaceActivity.class.getSimpleName();
    public ScaleRatingBar mVendorRatingBar;
    public String mVendorPhoneNumber = "";
    public FloatingActionButton mVendorCallButton;
    public FloatingActionButton mVendorBookmarkButton;
    public FloatingActionButton location;
    public ImageView mVendorImageView;
    public TextView name;
    public TextView mVendorAddress;
    public TextView mVendorFoodItems;
    public String number;
    public TextView mVendorTime;
    public IRetrofitDataApi apiInterface;
    public double newRatings;
    public float oldRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_place);

        mVendorImageView = findViewById(R.id.firebasePlaceImageView);
        mVendorRatingBar = findViewById(R.id.night_simpleRatingBar);
        mVendorTime = findViewById(R.id.night_time);

        Picasso.with(this)
                .load(getIntent().getStringExtra("vendor_url"))
                .into(mVendorImageView); //Setting the vendor's image

        name = findViewById(R.id.night_placeNameTextView);
        mVendorAddress = findViewById(R.id.night_address);
        mVendorCallButton = findViewById(R.id.night_callButton);
        mVendorBookmarkButton = findViewById(R.id.night_bookmarkButton);
        location = findViewById(R.id.night_mapsButton);

        mVendorFoodItems = findViewById(R.id.night_foodType);
        mVendorFoodItems.setText("Food Items: " + getIntent().getStringExtra("vendor_food"));
        //Setting the food items available at the vendor


        mVendorTime.setText(String.format("Time: %dAM TO %dAM",
                getIntent().getIntExtra("vendor_open", 0),
                getIntent().getIntExtra("vendor_close", 0)));
        //Setting the vendor's time

        apiInterface = APIClient.getClient().create(IRetrofitDataApi.class);

        name.setText(getIntent().getStringExtra("vendor_name")); //Setting vendor's name
        mVendorRatingBar.setRating( getIntent().getFloatExtra("vendor_ratings", 0f));
        //Setting the initial ratings of the vendor

        double latitude = getIntent().getDoubleExtra("vendor_lat", 0);
        double longitude = getIntent().getDoubleExtra("vendor_lng", 0);

        getAddress(latitude, longitude); //Getting the address of the vendor using its latitude and longitude

        mVendorPhoneNumber = getIntent().getStringExtra("vendor_phone");

        apiInterface =  APIClient.getClient().create(IRetrofitDataApi.class);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.night_map);
        mapFragment.getMapAsync(this);

        //While clicking on the ratings
        mVendorRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                mVendorRatingBar.setClickable(false); //First set the ratings to non-clickable
                oldRatings = mVendorRatingBar.getRating(); //Get the old ratings
                String vendorName = name.getText().toString(); // Get the vendors name
                double mNewRatings = sendRatings(vendorName,v + oldRatings); // Getting the new ratings + old ratings
                mVendorRatingBar.setRating((float)(mNewRatings + oldRatings)); //Setting the new ratings
            }
        });

        mVendorCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(mVendorPhoneNumber); //Call the vendor
            }
        });

        mVendorBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBookmark placeBookmark = new PlaceBookmark(getIntent().getStringExtra("vendor_id"),
                        getIntent().getExtras().getString("vendor_name"),
                        getIntent().getStringExtra("vendor_url"));

                placeBookmark.save();

                Toast.makeText(FirebasePlaceActivity.this, "Bookmark added!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private double sendRatings(String name, float v) {
        //Sending the ratings to the firebase real-time database
        Call<RatingsResponse> call = apiInterface.saveRatings(name, v); //Creating a request call to the firebase
        call.enqueue(new Callback<RatingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RatingsResponse> call, @NonNull Response<RatingsResponse> response) {
            List<Double> r = response.body().getRatings(); //Getting response in form of JSON
                newRatings = r.get(0); //Getting the first element
            }

            @Override
            public void onFailure(@NonNull Call<RatingsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: server: " + t.getLocalizedMessage());
            }
        });

        return newRatings; //Returning the new ratings
    }

    public String getVendorName () {
        return name.getText().toString();
    }

    private void getAddress(double latitude, double longitude) {
        //Getting address of the location
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH); //Creating a geocoder object
        try {
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            // If any additional address line present than only,
            // check with max available address lines by getMaxAddressLineIndex()

            mVendorAddress.setText("Address: " + address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void callPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL); //Redirecting to the Dialer Application
        intent.setData(Uri.parse("tel: " + phoneNumber)); //Getting the phone number

        if (Build.VERSION.SDK_INT >= 23 ) { //If higher than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Checking for the permissions
            }
            startActivity(intent); //Start Dialer application
        }
        else {  //If lower than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Check for permissions
            }
            startActivity(intent); //Start Dialer application
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
            //Checking for permissions
        }
        googleMap.setMyLocationEnabled(true); //Setting user's current location in blue dot

        LatLng latLng = new LatLng(getIntent().getExtras().getDouble("vendor_lat"),
                getIntent().getExtras().getDouble("vendor_lng"));
        //Creating a location using its latitude and longitude

        //Adding place name to the place marker
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(getIntent().getExtras().getString("vendor_name"))).showInfoWindow();

        //Adding zoom to the maps
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }
}