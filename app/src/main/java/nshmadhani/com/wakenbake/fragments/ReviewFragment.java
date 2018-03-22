package nshmadhani.com.wakenbake.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nshmadhani.com.wakenbake.activities.FirebasePlaceActivity;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.models.APIClient;
import nshmadhani.com.wakenbake.models.UserReview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private TextView mReviewHeading;
    private Button mSubmitButton;
    private EditText mReviewBody;
    private FirebasePlaceActivity mActivity;
    private String mUsername = "";
    private String mVendorName = "";
    public IRetrofitDataApi apiInterface;
    public static final String TAG = ReviewFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_fragment, container, false);

        mReviewHeading = rootView.findViewById(R.id.reviewHeading);
        mReviewBody = rootView.findViewById(R.id.reviewEditText);
        mSubmitButton = rootView.findViewById(R.id.submitButton);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveReview(mUsername, mReviewBody.getText().toString(), mVendorName);
                    }
                });


            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (FirebasePlaceActivity) getActivity();
        apiInterface = APIClient.getClient().create(IRetrofitDataApi.class);
    }

    private void saveReview(String mUsername, String s, String mVendorName) {
        final UserReview userReview = new UserReview(mUsername, s, mVendorName);
        Call<UserReview> userReviewCall = apiInterface.saveReview(userReview);
        userReviewCall.enqueue(new Callback<UserReview>() {
            @Override
            public void onResponse(Call<UserReview> call, Response<UserReview> response) {
                UserReview review = response.body();

                if (review != null) {
                    Log.d(TAG, "ReviewResponse: " + review);
                    Log.d(TAG, "Username: " + review.getmUsername());
                    Log.d(TAG, "Review: " + review.getmReview());
                    Log.d(TAG, "Vendor: " + review.getmVendorName());

                    String responseCode = Integer.toString(response.code());

                    if (responseCode != null && responseCode.equals("404")) {
                        Toast.makeText(mActivity, "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "Welcome " + review.getmUsername(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserReview> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                call.cancel();
            }
        });
    }
}
