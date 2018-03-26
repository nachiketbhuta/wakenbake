package nshmadhani.com.wakenbake.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nshmadhani.com.wakenbake.Models.UserReviewResponse;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Activities.TiffinPlacesActivity;
import nshmadhani.com.wakenbake.Interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.Models.APIClient;
import nshmadhani.com.wakenbake.Models.TiffinReview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private TextView mReviewHeading;
    private Button mSubmitButton;
    private EditText mReviewBody;
    private String mUsername = "abc@gmail.com";
    private String mVendorName = "Damodar";
    public IRetrofitDataApi apiInterface;
    public ListView mReviewsListView;
    public ArrayAdapter<String> mReviewsListAdapter;
    public static final String TAG = ReviewFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_fragment, container, false);

        mReviewHeading = rootView.findViewById(R.id.reviewHeading);
        mReviewsListView = rootView.findViewById(R.id.mReviewFragmentListView);
        mReviewBody = rootView.findViewById(R.id.reviewEditText);
        mSubmitButton = rootView.findViewById(R.id.submitButton);

//        mReviewsListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, msg);
//        mReviewsListView.setAdapter(mReviewsListAdapter);



        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getReviews(mVendorName);
                        //saveReview(mUsername, mReviewBody.getText().toString(), mVendorName);
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = APIClient.getClient().create(IRetrofitDataApi.class);
    }

    private void getReviews(String mVendorName) {
        Call<UserReviewResponse> call = apiInterface.getReviews(mVendorName);
        call.enqueue(new Callback<UserReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserReviewResponse> call, @NonNull Response<UserReviewResponse> response) {
                //String userReviewResponseList = response.body().getReview();
                //String username = response.body().getUsername();
                Log.d(TAG, "onResponse: getReviews error: " + response.errorBody());
                Log.d(TAG, "onResponse: getReviews message: " + response.message());
                Log.d(TAG, "onResponse: getReviews response: " + response.body());
            }

            @Override
            public void onFailure(Call<UserReviewResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: getReviews: " + t);
            }
        });
    }

    private void saveReview(String mUsername, String s, String mVendorName) {
        final TiffinReview tiffinReview = new TiffinReview(mUsername, s, mVendorName);
        Call<TiffinReview> tiffinReviewCall = apiInterface.saveTiffinUserReview(tiffinReview);
        tiffinReviewCall.enqueue(new Callback<TiffinReview>() {
            @Override
            public void onResponse(Call<TiffinReview> call, Response<TiffinReview> response) {
                TiffinReview review = response.body();
                Log.d(TAG, "onResponse: server review: " +response.body());

                if (review != null) {
                    Log.d(TAG, "ReviewResponse: " + review);
                    Log.d(TAG, "Username: " + review.getmTiffinName());
                    Log.d(TAG, "Review: " + review.getmTiffinReview());
                    Log.d(TAG, "Vendor: " + review.getmTiffinProvider());

                    String responseCode = Integer.toString(response.code());

                    if (responseCode != null && responseCode.equals("404")) {
                        Toast.makeText(getActivity(), "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Welcome " + review.getmTiffinName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TiffinReview> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
                //call.cancel();
            }
        });
    }
}
