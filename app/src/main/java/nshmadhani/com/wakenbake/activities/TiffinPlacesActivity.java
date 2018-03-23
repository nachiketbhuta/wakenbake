package nshmadhani.com.wakenbake.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.models.PlaceBookmark;

public class TiffinPlacesActivity extends AppCompatActivity {

    public static final String TAG = TiffinPlacesActivity.class.getSimpleName();
    public ImageView tiffinImage;
    public TextView tiffinName;
    public TextView tiffinFoodItems;
    public ScaleRatingBar tiffinRatings;
    public FloatingActionButton tiffinCall;
    public FloatingActionButton tiffinBookmark;

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
                .load(R.drawable.no_image)
                .into(tiffinImage);

        tiffinName.setText(getIntent().getStringExtra("tiffin_name"));
        tiffinFoodItems.setText(getIntent().getStringExtra("tiffin_food"));
        tiffinCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(getIntent().getStringExtra("tiffin_number"));
            }
        });

        tiffinRatings.setRating(getIntent().getFloatExtra("tiffin_ratings", 0));

        tiffinRatings.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                changeRatings(v);
            }
        });

        tiffinBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBookmark placeBookmark = new PlaceBookmark(getIntent().getExtras().getString("tiffin_name"));

                placeBookmark.save();

                Toast.makeText(TiffinPlacesActivity.this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Bookmarks " + placeBookmark.getPlaceID() + ", " + placeBookmark.getPlaceNAME());

            }
        });
    }

    private void changeRatings(float v) {

    }

    private void call(String tiffin_number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + tiffin_number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= 23 ) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
            else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }
    }
}
