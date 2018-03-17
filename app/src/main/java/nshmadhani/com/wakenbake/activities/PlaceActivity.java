package nshmadhani.com.wakenbake.activities;

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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
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
import com.orm.SugarContext;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.models.PlaceBookmark;

public class PlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PlaceActivity.class.getSimpleName();
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
        //ratingBar.setRating(1.3f);
        placeName = findViewById(R.id.placeNameTextView);
        address = findViewById(R.id.address);
        website = findViewById(R.id.website);

        SugarContext.init(this);

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
                callPlace(phoneNumber);
                Toast.makeText(PlaceActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        location = findViewById(R.id.mapsButton);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlaceActivity.this, "Slide to Bottom for location", Toast.LENGTH_SHORT).show();
            }
        });

        bookmark = findViewById(R.id.bookmarkButton);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBookmark placeBookmark = new PlaceBookmark(getIntent().getStringExtra("placeId"),
                        getIntent().getExtras().getString("placeName"));

                PlaceBookmark.save(placeBookmark);

                List<PlaceBookmark> placeBookmarks = PlaceBookmark.listAll(PlaceBookmark.class);

                for (PlaceBookmark p : placeBookmarks) {
                    String msg = String.format("%s, %s", p.getPlaceID(), p.getPlaceNAME());
                    Toast.makeText(PlaceActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void callPlace(String phoneNumber) {
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

    private void getDetailsOfPlaces(String placeId) {
        mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place myPlace = places.get(0);
                    address.setText("Address: " + myPlace.getAddress());
                    if (myPlace.getWebsiteUri() == null)
                        website.setText("Website: No Website Yet");
                    else
                        website.setText("Website: " + myPlace.getWebsiteUri());
                    phoneNumber += String.valueOf(myPlace.getPhoneNumber());
                    places.release();
                } else {
                    Log.e(TAG, "Place not found.");
                }
            }
        });

    }

    private void getPhotosOfPlaces(String placeId) {

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    // Get the list of photos.
                    //Log.d(TAG, "Photo response" + );
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
                            imageFlipper.addView(image);
                            imageFlipper.setFlipInterval(3000); //5s intervals
                            imageFlipper.startFlipping();
                        }
                    });
                }
            }
        });
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

        LatLng latLng = new LatLng(getIntent().getExtras().getDouble("placeLatitude"),
                getIntent().getExtras().getDouble("placeLongitude"));

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
