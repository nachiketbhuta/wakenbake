package nshmadhani.com.wakenbake.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ViewFlipper;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.willy.ratingbar.ScaleRatingBar;

import nshmadhani.com.wakenbake.Models.PlaceBookmark;
import nshmadhani.com.wakenbake.Models.WakeNBake;
import nshmadhani.com.wakenbake.R;

public class GooglePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = GooglePlacesActivity.class.getSimpleName();
    private GeoDataClient mGeoDataClient;
    public ViewFlipper imageFlipper;
    public TextView placeName;
    public ScaleRatingBar ratingBar;
    public String phoneNumber = "";
    public TextView address;
    public TextView website;
    public FloatingActionButton call;
    public FloatingActionButton bookmark;
    public FloatingActionButton location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        String placeId = getIntent().getStringExtra("placeId");
        imageFlipper = findViewById(R.id.image_flipper);
        ratingBar = findViewById(R.id.simpleRatingBar);
        ratingBar.setRating((float) getIntent().getDoubleExtra("placeRatings", 0));
        placeName = findViewById(R.id.placeNameTextView);
        address = findViewById(R.id.address);
        website = findViewById(R.id.website);

        //SugarContext.init(this);

        ratingBar.setClickable(false);
        placeName.setText(getIntent().getStringExtra("placeName"));

        getDetailsOfPlaces(placeId);
        getPhotosOfPlaces(placeId);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        call = findViewById(R.id.callButton);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPlace(phoneNumber); //Call to the place using its phone number
                Log.d(TAG, "onClick: call: ");
                Toast.makeText(GooglePlacesActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        location = findViewById(R.id.mapsButton);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GooglePlacesActivity.this, "Slide to Bottom for location", Toast.LENGTH_SHORT).show();
            }
        });

        bookmark = findViewById(R.id.bookmarkButton);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adding to the database
                String id = getIntent().getStringExtra("placeId");
                String name = getIntent().getStringExtra("placeName");
                String url = getIntent().getStringExtra("placeUrl");
                PlaceBookmark placeBookmark = new PlaceBookmark(id, name, url);

                String foundPlace = WakeNBake.database.iDoa().checkInDatabase(id);

                if (name.equalsIgnoreCase(foundPlace)) {
                    Toast.makeText(GooglePlacesActivity.this, "Already added to bookmarks!", Toast.LENGTH_SHORT).show();
                }
                else {
                    WakeNBake.database.iDoa().addPlace(placeBookmark);
                    Toast.makeText(GooglePlacesActivity.this, "Bookmark added!", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(GooglePlacesActivity.this, "Bookmark added!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callPlace(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL); //Redirecting to the Dialer Application
        intent.setData(Uri.parse("tel: " + phoneNumber)); //Getting the phone number
        if (Build.VERSION.SDK_INT >= 23 ) { //If higher than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Checking for the permissions
            }
            startActivity(intent); //Start Dialer application
        }
        else { //If lower than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Check for permissions
            }
            startActivity(intent); //Start Dialer application
        }
    }

    private void getDetailsOfPlaces(String placeId) {
        //Getting place details using its ID from Google Places API
        mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place myPlace = places.get(0); //Got the place
                    address.setText("Address: " + myPlace.getAddress()); //Setting address
                    if (myPlace.getWebsiteUri() == null)
                        website.setText("Website: No Website Yet");
                    else
                        website.setText("Website: " + myPlace.getWebsiteUri()); //Setting website
                    phoneNumber += String.valueOf(myPlace.getPhoneNumber());
                    places.release();
                } else {
                    Log.e(TAG, "Place not found.");
                }
            }
        });

    }

    private void getPhotosOfPlaces(String placeId) {

        //Getting more than one photo of a place using its ID

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    for (int i = 0; i < photoMetadataBuffer.getCount(); i++) {
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            ImageView image = new ImageView(getApplicationContext());
                            image.setImageBitmap(bitmap);
                            imageFlipper.addView(image); //Adding image to the ViewFlipper
                            imageFlipper.setFlipInterval(3000); //3s intervals
                            imageFlipper.startFlipping();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Called when map fragment is ready

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
            //Checking for permissions
        }
        googleMap.setMyLocationEnabled(true); //Setting user's current location in blue dot

        LatLng latLng = new LatLng(getIntent().getExtras().getDouble("placeLatitude"),
                getIntent().getExtras().getDouble("placeLongitude"));
        //Creating a location using its latitude and longitude

        //Adding place name to the place marker
        googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(getIntent().getExtras().getString("placeName"))).showInfoWindow();

        //Adding zoom to the maps
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
