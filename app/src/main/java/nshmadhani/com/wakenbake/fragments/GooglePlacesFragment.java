package nshmadhani.com.wakenbake.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.adapters.GooglePlacesListAdapter;
import nshmadhani.com.wakenbake.activities.NavigationActivity;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.models.GooglePlaces;
public class GooglePlacesFragment extends android.support.v4.app.Fragment {

    private static  List<GooglePlaces> mGooglePlacesList;
    private RecyclerView mGooglePlacesRecyclerView;
    private GooglePlacesListAdapter mGooglePlacesListAdapter;
    public static String apiKey = "AIzaSyBiREqfd8QWqyeTii3djqE0IhVmKCfoHjs";
    private NavigationActivity mActivity;
    private FirebaseAuth mAuth;
    public static final String TAG = GooglePlacesFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.google_places_fragment, container, false);

        mGooglePlacesList = new ArrayList<>();
        mGooglePlacesRecyclerView  = rootView.findViewById(R.id.googlePlacesFragment);
        mGooglePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mGooglePlacesListAdapter = new GooglePlacesListAdapter(mGooglePlacesList, mActivity);
        mGooglePlacesRecyclerView.setAdapter(mGooglePlacesListAdapter);

        final Intent intent = mActivity.getIntent();

        final double latitude = intent.getDoubleExtra("latitude", 0.0);
        final double longitude = intent.getDoubleExtra("longitude", 0.0);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPlacesFromGoogle(latitude, longitude);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (NavigationActivity) getActivity();
        mAuth = mActivity.getmAuth();

    }


    private void getPlacesFromGoogle(final double latitude, final double longitude) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        NearbySearchRequest nearbySearchRequest = new NearbySearchRequest(geoApiContext);

        nearbySearchRequest
                .location(new LatLng(latitude, longitude))
                .type(PlaceType.RESTAURANT , PlaceType.CAFE ,PlaceType.FOOD ,PlaceType.BAKERY)
                .openNow(true)
                .rankby(RankBy.DISTANCE)
                .setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                @Override
                public void onResult(PlacesSearchResponse result) {
                    for (PlacesSearchResult place : result.results) {
                        GooglePlaces googlePlaces = new GooglePlaces();
                        googlePlaces.setName(place.name);
                        googlePlaces.setLatitude(place.geometry.location.lat);
                        googlePlaces.setLongitude(place.geometry.location.lng);
                        googlePlaces.setPlaceId(place.placeId);
                        googlePlaces.setRatings(place.rating);
                        Log.d(TAG, "onResult: " + place.placeId);
                        String URL = "";

                        try {
                            URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                    + place.photos[0].photoReference + "&key=" + apiKey;
                        } catch (Exception e) {
                            URL = "";
                        }
                        googlePlaces.setImageUrl(URL);
                        //googlePlaces.addToMasterList(place.name);
                        Log.d(TAG, "onResult: "+ googlePlaces.getName() + googlePlaces.getRatings());
//                            Log.d(TAG, "onResult: Reference " + googlePlaces.getPhotoReference());
                        mGooglePlacesList.add(googlePlaces);
                        NavigationActivity.mMasterPlaceList.add(googlePlaces);
                    }
                    Log.d(TAG, "onResult: "+ mGooglePlacesList.size());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGooglePlacesListAdapter.notifyDataSetChanged();
                        }
                    });

                }
                @Override
                public void onFailure(Throwable e) {
                    Log.e(TAG , "onFailure: ", e);
                }
            });
    }
}
