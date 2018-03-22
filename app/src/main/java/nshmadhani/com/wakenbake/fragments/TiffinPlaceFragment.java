package nshmadhani.com.wakenbake.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.activities.NavigationActivity;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.holders.TiffinPlacesHolder;
import nshmadhani.com.wakenbake.adapters.TiffinPlacesListAdapter;
import nshmadhani.com.wakenbake.interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.models.TiffinPlaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TiffinPlaceFragment extends android.support.v4.app.Fragment{

    public static final String TAG = TiffinPlaceFragment.class.getSimpleName();
    private FirebaseAuth mAuth;
    private NavigationActivity mActivity;
    private static List<TiffinPlaces> mTiffinPlacesList;
    private TiffinPlacesListAdapter mTiffinPlacesListAdapter;
    private RecyclerView mTiffinPlacesRecyclerView;

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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getTiffinPlacesFromFirebase();
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

    private void getTiffinPlacesFromFirebase() {
        final Gson gson;
        gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRetrofitDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IRetrofitDataApi apiInterface = retrofit.create(IRetrofitDataApi.class);
        Call<TiffinPlacesHolder> call = apiInterface.getmTiffinPlaces("");
        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<TiffinPlacesHolder>() {
            @Override
            public void onResponse(@NonNull Call<TiffinPlacesHolder> call, @NonNull Response<TiffinPlacesHolder> response) {
                List<TiffinPlaces> mTiffinPlaces = response.body().getmTiffinPlaces();
                if (mTiffinPlaces != null) {
                    for (TiffinPlaces t : mTiffinPlaces) {
                        TiffinPlaces tiffinPlaces = new TiffinPlaces();
                        tiffinPlaces.setmTiffinName(t.mTiffinName);
                        tiffinPlaces.setmTiffinNumber(t.mTiffinNumber);
                        tiffinPlaces.setmTiffinFoodItems(t.mTiffinFoodItems);
                        tiffinPlaces.setmTiffinRatings(t.mTiffinRatings);
                        //tiffinPlaces.addToMasterList(t.getmTiffinName());
                        Log.d(TAG, "onResponse: " + tiffinPlaces.getmTiffinName());
                        mTiffinPlacesList.add(t);
                        NavigationActivity.mMasterPlaceList.add(tiffinPlaces);
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
                Log.e(TAG, "onFailure: ", t );
            }
        });


    }
}
