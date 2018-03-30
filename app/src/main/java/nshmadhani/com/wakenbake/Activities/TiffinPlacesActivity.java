package nshmadhani.com.wakenbake.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import nshmadhani.com.wakenbake.Adapters.ReviewFragmentListAdapter;
import nshmadhani.com.wakenbake.Holders.ReviewResponse;
import nshmadhani.com.wakenbake.Models.Review;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Interfaces.IRetrofitDataApi;
import nshmadhani.com.wakenbake.Models.APIClient;
import nshmadhani.com.wakenbake.Models.PlaceBookmark;
import nshmadhani.com.wakenbake.Holders.RatingsResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiffinPlacesActivity extends AppCompatActivity {

    public static final String TAG = TiffinPlacesActivity.class.getSimpleName();
    public ImageView tiffinImage;
    public TextView tiffinName;
    public TextView tiffinFoodItems;
    public ScaleRatingBar tiffinRatings;
    public FloatingActionButton tiffinCall;
    public FloatingActionButton tiffinBookmark;
    private float oldRatings;
    private IRetrofitDataApi apiInterface;
    private double newRatings;
    private List<Review> mReviewList;
    public RecyclerView mReviewsRecyclerView;
    public ReviewFragmentListAdapter mReviewsListAdapter;
    private TextView mReviewHeading;
    private Button mSubmitButton;
    private EditText mReviewBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_places);

        tiffinImage= findViewById(R.id.tiffinPlaceImageView);
        tiffinName = findViewById(R.id.tiffin_placeNameTextView);
        tiffinFoodItems = findViewById(R.id.tiffin_foodType);
        tiffinRatings = findViewById(R.id.tiffin_simpleRatingBar);
        tiffinBookmark = findViewById(R.id.tiffin_bookmarkButton);
        tiffinCall = findViewById(R.id.tiffin_callButton);

        Picasso.with(this)
                .load(getIntent().getStringExtra("tiffin_url"))
                .into(tiffinImage);

        tiffinName.setText(getIntent().getStringExtra("tiffin_name")); //Setting the name
        tiffinFoodItems.setText("Food Items: " + getIntent().getStringExtra("tiffin_food")); //Setting the food items
        tiffinCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(getIntent().getStringExtra("tiffin_number")); //Call the vendor
            }
        });

        apiInterface =  APIClient.getClient().create(IRetrofitDataApi.class);

        tiffinRatings.setRating(getIntent().getFloatExtra("tiffin_ratings", 0));
        //Setting the initial ratings of the vendor

        tiffinRatings.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                tiffinRatings.setClickable(false); //First set the ratings to non-clickable
                oldRatings = tiffinRatings.getRating(); //Get the old ratings
                String vendorName = tiffinName.getText().toString(); // Get the vendors name
                double mNewRatings = changeRatings(vendorName,v + oldRatings); // Getting the new ratings + old ratings
                tiffinRatings.setRating((float)(mNewRatings + oldRatings)); //Setting the new ratings
            }
        });

        tiffinBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBookmark placeBookmark = new PlaceBookmark(getIntent().getStringExtra("tiffin_id"),
                        getIntent().getExtras().getString("tiffin_name") ,
                        getIntent().getStringExtra("tiffin_url"));

                placeBookmark.save();

                Toast.makeText(TiffinPlacesActivity.this, "Bookmark added!", Toast.LENGTH_SHORT).show();
            }
        });

        mReviewHeading = findViewById(R.id.reviewHeading);
        mReviewBody = findViewById(R.id.reviewEditText);
        mSubmitButton = findViewById(R.id.submitButton);

        mReviewList = new ArrayList<>();
        mReviewsRecyclerView  = findViewById(R.id.mReviewRecyclerView);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsListAdapter = new ReviewFragmentListAdapter(mReviewList,this);
        mReviewsRecyclerView.setAdapter(mReviewsListAdapter);

        getReviews(tiffinName.getText().toString());

        final String mUsername = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TiffinPlacesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveReview(mUsername, mReviewBody.getText().toString(), tiffinName.getText().toString());
                    }
                });
            }
        });
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

                TiffinPlacesActivity.this.runOnUiThread(new Runnable() {
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
        Call<ResponseBody> saveCall = apiInterface.saveReviews(s, mUsername, mVendorName);
        saveCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    Toast.makeText(TiffinPlacesActivity.this, "Review added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TiffinPlacesActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private double changeRatings(String name, float v) {
        //Sending the ratings to the firebase real-time database
        Call<RatingsResponse> call = apiInterface.saveTiffinRatings(name, v);  //Creating a request call to the firebase
        call.enqueue(new Callback<RatingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RatingsResponse> call, @NonNull Response<RatingsResponse> response) {
                List<Double> r = response.body().getRatings(); //Getting response in form of JSON
                newRatings = r.get(0); //Getting the first element
            }

            @Override
            public void onFailure(Call<RatingsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: server: " + t.getLocalizedMessage());
            }
        });

        return newRatings; //Returning the new ratings
    }


    private void call(String tiffin_number) {
        Intent intent = new Intent(Intent.ACTION_DIAL); //Redirecting to the Dialer Application
        intent.setData(Uri.parse("tel: " + tiffin_number)); //Getting the phone number

        if (Build.VERSION.SDK_INT >= 23 ) { //If higher than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Checking for the permissions
            }
            startActivity(intent); //Start Dialer application
        }
        else { //If lower than Android M
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
                //Check for permissions
            }
            startActivity(intent); //Start Dialer application
        }
    }
}
