package nshmadhani.com.wakenbake.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.Adapters.ReviewFragmentListAdapter;
import nshmadhani.com.wakenbake.Holders.ReviewResponse;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.Models.APIClient;
import nshmadhani.com.wakenbake.Models.Review;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private TextView mReviewHeading;
    private Button mSubmitButton;
    private EditText mReviewBody;
    private String mUsername = "abc@gmail.com";
    private String mVendorName = "";
    public IRetrofitDataApi apiInterface;
    public RecyclerView mReviewsRecyclerView;
    public ReviewFragmentListAdapter mReviewsListAdapter;
    public static final String TAG = ReviewFragment.class.getSimpleName();
    private List<Review> mReviewList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_fragment, container, false);

        mReviewHeading = rootView.findViewById(R.id.reviewHeading);
        mReviewBody = rootView.findViewById(R.id.reviewEditText);
        mSubmitButton = rootView.findViewById(R.id.submitButton);

        mReviewList = new ArrayList<>();
        mReviewsRecyclerView  = rootView.findViewById(R.id.mReviewRecyclerView);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewsListAdapter = new ReviewFragmentListAdapter(mReviewList, getActivity());
        mReviewsRecyclerView.setAdapter(mReviewsListAdapter);

        mVendorName = getArguments().getString("vendor_name");

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

        Call<ReviewResponse> call = apiInterface.getReviews(mVendorName);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                List<Review> reviewResponseList = response.body().getmReviewData();
                for (Review r : reviewResponseList) {
                    Review review = new Review();
                    review.setmUsernameReview(r.getmUsernameReview());
                    review.setmReview(r.getmReview());

                    mReviewList.add(r);
                }

                Log.d(TAG, "onResponse: list size: " + mReviewList.size());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReviewsListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }

    private void saveReview(String mUsername, String s, String mVendorName) {
        //Call<ResponseBody> saveCall = apiInterface.
    }
}