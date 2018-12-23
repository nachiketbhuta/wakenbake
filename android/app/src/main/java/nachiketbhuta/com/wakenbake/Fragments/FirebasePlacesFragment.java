package nachiketbhuta.com.wakenbake.Fragments;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.List;

import nachiketbhuta.com.wakenbake.Holders.FirebasePlacesHolder;
import nachiketbhuta.com.wakenbake.Adapters.FirebasePlacesListAdapter;
import nachiketbhuta.com.wakenbake.Activities.NavigationActivity;
import nachiketbhuta.com.wakenbake.R;
import nachiketbhuta.com.wakenbake.Interfaces.IRetrofitDataApi;
import nachiketbhuta.com.wakenbake.Models.APIClient;
import nachiketbhuta.com.wakenbake.Models.FirebasePlaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebasePlacesFragment extends android.support.v4.app.Fragment {

    public static List<FirebasePlaces> mFirebasePlacesList;
    private NavigationActivity mActivity;
    private RecyclerView mFirebasePlacesRecyclerView;
    private FirebaseAuth mAuth;
    private FirebasePlacesListAdapter mFirebasePlacesListAdapter;
    public static final String TAG = FirebasePlacesFragment.class.getSimpleName();
    public IRetrofitDataApi iRetrofitDataApi;
    public ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.firebase_places_fragment, container, false);

        mFirebasePlacesList = new ArrayList<>();
        mFirebasePlacesRecyclerView  = rootView.findViewById(R.id.firebasePlacesRecyclerView);
        mFirebasePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mFirebasePlacesListAdapter = new FirebasePlacesListAdapter(mFirebasePlacesList, mActivity);
        mFirebasePlacesRecyclerView.setAdapter(mFirebasePlacesListAdapter);

        progressDialog = new ProgressDialog(mActivity);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                getPlacesFromFirebaseDatabase();
            }
        });

        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        String firebase = (savedInstanceState != null) ? savedInstanceState.getString("firebase") : "null";
        Log.i(TAG, " onViewStateRestored: " + firebase);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, " onSaveInstanceState.");
        outState.putString("firebase", "FirebasePlaces");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setAdapter(){
        if(NavigationActivity.isSearched){
            List<FirebasePlaces> temp= NavigationActivity.mMaster.getNight();
            mFirebasePlacesListAdapter = new FirebasePlacesListAdapter(temp, mActivity);
            mFirebasePlacesRecyclerView.setAdapter(mFirebasePlacesListAdapter);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationActivity.fragF=this;
        mActivity = (NavigationActivity) getActivity();
        mAuth = mActivity.getmAuth();

        iRetrofitDataApi = APIClient.getClient().create(IRetrofitDataApi.class);
    }

    private void getPlacesFromFirebaseDatabase() {

        Call<FirebasePlacesHolder> call = iRetrofitDataApi.getPlacesFromFirebase("");
        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<FirebasePlacesHolder>() {
            @Override
            public void onResponse(@NonNull Call<FirebasePlacesHolder> call, @NonNull Response<FirebasePlacesHolder> response) {
                progressDialog.dismiss();
                List<FirebasePlaces> firebasePlacesList = response.body().getmPlaces();
                if (firebasePlacesList != null) {
                    for(FirebasePlaces f : firebasePlacesList) {
                        final FirebasePlaces mFirebasePlaces = new FirebasePlaces();
                        mFirebasePlaces.setmVendorName(f.mVendorName);
                        mFirebasePlaces.setmVendorPhoneNumber(f.mVendorPhoneNumber);
                        mFirebasePlaces.setmVendorLatitude(f.mVendorLatitude);
                        mFirebasePlaces.setmVendorLongitude(f.mVendorLongitude);
                        mFirebasePlaces.setmVendorRatings(f.mVendorRatings);
                        mFirebasePlaces.setmVendorId(f.mVendorId);
                        mFirebasePlaces.setmVendorOpenTime(f.mVendorOpenTime);
                        mFirebasePlaces.setmVendorCloseTime(f.mVendorCloseTime);
                        mFirebasePlaces.setmVendorFoodItems(f.mVendorFoodItems);

                        if (f.mVendorUrl == null) {
                            mFirebasePlaces.setmVendorUrl("");
                        } else {
                            mFirebasePlaces.setmVendorUrl(f.mVendorUrl);
                        }
                        Log.d(TAG, "onResponse: " + f.getmVendorId());
                        Log.d(TAG, "onResult: "+f.getmVendorName());
                        Log.d(TAG, "onResponse: url : " + f.mVendorUrl);
                        mFirebasePlacesList.add(f);
                    }
                }
                Log.d(TAG, "onResult: "+ mFirebasePlacesList.size());
                Log.d(TAG, "onResponse: "+response.body());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFirebasePlacesListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<FirebasePlacesHolder> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: ",t);
                Toast.makeText(mActivity, "Server Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

