package nshmadhani.com.wakenbake.Fragments;

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

import nshmadhani.com.wakenbake.Activities.NavigationActivity;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Holders.TiffinPlacesHolder;
import nshmadhani.com.wakenbake.Adapters.TiffinPlacesListAdapter;
import nshmadhani.com.wakenbake.Interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.Models.APIClient;
import nshmadhani.com.wakenbake.Models.TiffinPlaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiffinPlaceFragment extends android.support.v4.app.Fragment{

    public static final String TAG = TiffinPlaceFragment.class.getSimpleName();
    private FirebaseAuth mAuth;
    private NavigationActivity mActivity;
    public IRetrofitDataApi apiInterface;
    public static List<TiffinPlaces> mTiffinPlacesList;
    private TiffinPlacesListAdapter mTiffinPlacesListAdapter;
    private RecyclerView mTiffinPlacesRecyclerView;
    public ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.tiffin_places_fragment, container, false);

        mTiffinPlacesList = new ArrayList<>();
        mTiffinPlacesRecyclerView  = rootView.findViewById(R.id.tiffinplacesRecyclerView);
        mTiffinPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mTiffinPlacesListAdapter = new TiffinPlacesListAdapter(mTiffinPlacesList, mActivity);
        mTiffinPlacesRecyclerView.setAdapter(mTiffinPlacesListAdapter);

        progressDialog = new ProgressDialog(mActivity);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                getTiffinPlacesFromFirebase();
            }
        });
        return rootView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setAdapter() {
        if(NavigationActivity.isSearched){
            List<TiffinPlaces> temp= NavigationActivity.mMaster.getTiffin();
            mTiffinPlacesListAdapter = new TiffinPlacesListAdapter(temp, mActivity);
            mTiffinPlacesRecyclerView.setAdapter(mTiffinPlacesListAdapter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationActivity.fragT = this;
        mActivity = (NavigationActivity) getActivity();
        mAuth = mActivity.getmAuth();

        apiInterface =  APIClient.getClient().create(IRetrofitDataApi.class);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        String tiffin = (savedInstanceState != null) ? savedInstanceState.getString("tiffin") : "null";
        Log.i(TAG, " onViewStateRestored: " + tiffin);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, " onSaveInstanceState.");
        outState.putString("tiffin", "TiffinPlaces");
    }

    private void getTiffinPlacesFromFirebase() {
        Call<TiffinPlacesHolder> call = apiInterface.getmTiffinPlaces("");
        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<TiffinPlacesHolder>() {
            @Override
            public void onResponse(@NonNull Call<TiffinPlacesHolder> call, @NonNull Response<TiffinPlacesHolder> response) {
                progressDialog.dismiss();
                List<TiffinPlaces> mTiffinPlaces = response.body().getmTiffinPlaces();
                if (mTiffinPlaces != null) {
                    for (TiffinPlaces t : mTiffinPlaces) {
                        TiffinPlaces tiffinPlaces = new TiffinPlaces();
                        tiffinPlaces.setmTiffinName(t.mTiffinName);
                        tiffinPlaces.setmTiffinNumber(t.mTiffinNumber);
                        tiffinPlaces.setmTiffinFoodItems(t.mTiffinFoodItems);
                        tiffinPlaces.setmTiffinRatings(t.mTiffinRatings);
                        tiffinPlaces.setmTiffinId(t.mTiffinId);
                        Log.d(TAG, "onResponse: " + tiffinPlaces.getmTiffinName());
                        mTiffinPlacesList.add(t);
                    }
                }
                Log.d(TAG, "onResult: tiffin"+ mTiffinPlacesList.size());
                Log.d(TAG, "onResponse: "+response.body());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTiffinPlacesListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Call<TiffinPlacesHolder> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: ", t );
                Toast.makeText(mActivity, "Server Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
