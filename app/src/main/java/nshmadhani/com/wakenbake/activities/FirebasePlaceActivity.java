package nshmadhani.com.wakenbake.activities;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.models.APIClient;
import nshmadhani.com.wakenbake.models.FirebasePlaces;
import nshmadhani.com.wakenbake.models.PlaceBookmark;
import nshmadhani.com.wakenbake.models.RatingsResponse;
import okhttp3.ResponseBody;
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
    public String number;
    public TextView mVendorTime;
    public IRetrofitDataApi apiInterface;
    private IRetrofitDataApi iRetrofitDataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_place);

        mVendorImageView = findViewById(R.id.firebasePlaceImageView);
        mVendorRatingBar = findViewById(R.id.night_simpleRatingBar);
        mVendorTime = findViewById(R.id.night_time);

        Picasso.with(this)
                .load(getIntent().getStringExtra("vendor_url"))
                .into(mVendorImageView);

        name = findViewById(R.id.night_placeNameTextView);
        mVendorAddress = findViewById(R.id.night_address);
        mVendorCallButton = findViewById(R.id.night_callButton);
        mVendorBookmarkButton = findViewById(R.id.night_bookmarkButton);
        location = findViewById(R.id.night_mapsButton);
        mVendorTime.setText(String.format("Time: %dAM TO %dAM",
                getIntent().getIntExtra("vendor_open", 0),
                getIntent().getIntExtra("vendor_close", 0)));

        iRetrofitDataApi = APIClient.getClient().create(IRetrofitDataApi.class);

        name.setText(getIntent().getStringExtra("vendor_name"));
        mVendorRatingBar.setRating(getIntent().getFloatExtra("vendor_ratings" , 0));

        double latitude = getIntent().getDoubleExtra("vendor_lat", 0);
        double longitude = getIntent().getDoubleExtra("vendor_lng", 0);

        getAddress(latitude, longitude);

        mVendorPhoneNumber = getIntent().getStringExtra("vendor_phone");

        mVendorRatingBar.setRating( getIntent().getFloatExtra("vendor_ratings", 0f));



        apiInterface =  APIClient.getClient().create(IRetrofitDataApi.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.night_map);
        mapFragment.getMapAsync(this);

        mVendorRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                mVendorRatingBar.setClickable(true);
                String vendorName = name.getText().toString();
                float mNewRatings = (float)sendRatings(vendorName, v);
                mVendorRatingBar.setRating(mNewRatings);
            }
        });

        mVendorCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(mVendorPhoneNumber);
            }
        });

        mVendorBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBookmark placeBookmark = new PlaceBookmark(getIntent().getStringExtra("vendor_id"),
                        getIntent().getExtras().getString("vendor_name"),
                        getIntent().getStringExtra("vendor_url"));

                placeBookmark.save();

                Toast.makeText(FirebasePlaceActivity.this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Bookmarks " + placeBookmark.getPlaceID() + ", " + placeBookmark.getPlaceNAME());
            }
        });
    }

    private double sendRatings(String name, float v) {
        final RatingsResponse ratingsResponse = new RatingsResponse();
        Call<RatingsResponse> call = apiInterface.saveRatings(name, v);
        call.enqueue(new Callback<RatingsResponse>() {
            @Override
            public void onResponse(Call<RatingsResponse> call, Response<RatingsResponse> response) {
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(response.body().toString()).getAsJsonObject();
                Log.d(TAG, "onResponse: json : " + object.toString());
                ratingsResponse.setRatings(object.getAsDouble());
            }

            @Override
            public void onFailure(Call<RatingsResponse> call, Throwable t) {

            }
        });

        return ratingsResponse.getRatings();
    }

    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            Log.d("Address", "getAddress: "
                    + "Address: " + address
                    + "Known Name: " + knownName
                    + "City: " + city
                    + "State: " + state
                    + "Country: " + country
                    + "Postal Code : " + postalCode);

            mVendorAddress.setText("Address: " + address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void callPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= 23 ) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
            else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        LatLng latLng = new LatLng(getIntent().getExtras().getDouble("vendor_lat"),
                getIntent().getExtras().getDouble("vendor_lng"));

        //Adding place name to the place marker
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(getIntent().getExtras().getString("vendor_name"))).showInfoWindow();

        //Adding zoom to the maps
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }
}