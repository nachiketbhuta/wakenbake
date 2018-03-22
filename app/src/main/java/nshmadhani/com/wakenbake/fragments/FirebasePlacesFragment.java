package nshmadhani.com.wakenbake.fragments;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.holders.FirebasePlacesHolder;
import nshmadhani.com.wakenbake.adapters.FirebasePlacesListAdapter;
import nshmadhani.com.wakenbake.activities.NavigationActivity;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.models.FirebasePlaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebasePlacesFragment extends android.support.v4.app.Fragment {

    private static List<FirebasePlaces> mFirebasePlacesList;
    private NavigationActivity mActivity;
    private RecyclerView mFirebasePlacesRecyclerView;
    private FirebaseAuth mAuth;
    private FirebasePlacesListAdapter mFirebasePlacesListAdapter;
    public static final String TAG = FirebasePlacesFragment.class.getSimpleName();
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;

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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPlacesFromFirebaseDatabase();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (NavigationActivity) getActivity();
        mAuth = mActivity.getmAuth();

        firebaseStorage = mActivity.getmFirebaseStorage();
        storageReference = mActivity.getmStorageReference();

    }

    private void getPlacesFromFirebaseDatabase() {
        //Getting places from Firebase
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRetrofitDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IRetrofitDataApi apiInterface = retrofit.create(IRetrofitDataApi.class);
        Call<FirebasePlacesHolder> call = apiInterface.getPlacesFromFirebase("");
        Log.d(TAG, "onCreate: Connection successful");
        call.enqueue(new Callback<FirebasePlacesHolder>() {
            @Override
            public void onResponse(@NonNull Call<FirebasePlacesHolder> call, @NonNull Response<FirebasePlacesHolder> response) {
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

                        String imageFileName = f.mVendorName + ".jpg";

                        //Log.d(TAG, "onResponse: storage " + storageReference.child(imageFileName).toString());
                        storageReference.child(imageFileName.replaceAll("'", "")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: " + uri.toString());
                                //mFirebasePlaces.setImageURL(uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });

                        Log.d(TAG, "onResponse: " + f.getmVendorId());
                        Log.d(TAG, "onResult: "+f.getmVendorName());
                        mFirebasePlacesList.add(f);
                        NavigationActivity.mMasterPlaceList.add(f);
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
                Log.e(TAG, "onFailure: ",t);
            }
        });
    }
}
