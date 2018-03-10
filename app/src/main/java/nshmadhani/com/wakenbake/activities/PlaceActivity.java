package nshmadhani.com.wakenbake.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.location.places.GeoDataClient;
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

import nshmadhani.com.wakenbake.R;

public class PlaceActivity extends AppCompatActivity {

    //public SliderLayout sliderShow;
    private GeoDataClient mGeoDataClient;
    public ViewFlipper imageFlipper;
    public TextView placeName;
    public ScaleRatingBar ratingBar;

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

        placeName.setText(getIntent().getStringExtra("placeName"));

        getPhotosOfPlaces(placeId);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    private void getPhotosOfPlaces(String placeId) {

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
                            imageFlipper.addView(image);
                            imageFlipper.setFlipInterval(3000); //5s intervals
                            imageFlipper.startFlipping();
                        }
                    });
                }
            }
        });
    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        googleMap.setMyLocationEnabled(true);
//
//        LatLng latLng = new LatLng(getIntent().getExtras().getDouble("placeLatitude"),
//                getIntent().getExtras().getDouble("placeLongitude"));
//
//        //Adding place name to the place marker
//        googleMap.addMarker(new MarkerOptions().position(latLng)
//                    .title(getIntent().getExtras().getString("placeName"))).showInfoWindow();
//
//        //Adding zoom to the maps
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
//    }
}
