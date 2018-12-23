package nachiketbhuta.com.wakenbake.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import nachiketbhuta.com.wakenbake.Adapters.GooglePlacesListAdapter;
import nachiketbhuta.com.wakenbake.Activities.NavigationActivity;
import nachiketbhuta.com.wakenbake.R;
import nachiketbhuta.com.wakenbake.Models.GooglePlaces;

public class GooglePlacesFragment extends android.support.v4.app.Fragment {

    public static  List<GooglePlaces> mGooglePlacesList;
    private RecyclerView mGooglePlacesRecyclerView;
    private GooglePlacesListAdapter mGooglePlacesListAdapter;
    public static String apiKey = "AIzaSyBiREqfd8QWqyeTii3djqE0IhVmKCfoHjs";
    private NavigationActivity mActivity;
    private FirebaseAuth mAuth;
    private List<GooglePlaces> mMasterGooglePlaces;
    public static final String TAG = GooglePlacesFragment.class.getSimpleName();
    public ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(mActivity);

        final Intent intent = mActivity.getIntent();

        mMasterGooglePlaces = new ArrayList<>();

        final double latitude = intent.getDoubleExtra("latitude", 0.0);
        final double longitude = intent.getDoubleExtra("longitude", 0.0);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPlacesFromGoogle(latitude, longitude);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationActivity.fragG=this;
        mActivity = (NavigationActivity) getActivity();
        mAuth = mActivity.getmAuth();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, " onSaveInstanceState.");
        outState.putString("google", "GooglePlaces");
    }

    public void setAdapter(){
        if(NavigationActivity.isSearched){
            List<GooglePlaces> temp= NavigationActivity.mMaster.getDay();
            mGooglePlacesListAdapter = new GooglePlacesListAdapter(temp, mActivity);
            mGooglePlacesRecyclerView.setAdapter(mGooglePlacesListAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        String google = (savedInstanceState != null) ? savedInstanceState.getString("google") : "null";
        Log.i(TAG, " onViewStateRestored: " + google);
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
                    progressDialog.dismiss();
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
                        //NavigationActivity.mMasterPlaceList.add(googlePlaces);
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
                    progressDialog.dismiss();
                    Log.e(TAG , "onFailure: ", e);
                    Toast.makeText(mActivity, "Server Error!", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
