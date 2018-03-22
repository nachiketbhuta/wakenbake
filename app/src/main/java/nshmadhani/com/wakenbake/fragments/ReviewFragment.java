package nshmadhani.com.wakenbake.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nshmadhani.com.wakenbake.activities.FirebasePlaceActivity;
import nshmadhani.com.wakenbake.R;

public class ReviewFragment extends Fragment {

    private TextView mReviewHeading;
    private Button mSubmitButton;
    private EditText mReviewBody;
    private FirebasePlaceActivity mActivity;
    private String mUsername;
    private String mVendorName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_fragment, container, false);

        mReviewHeading = rootView.findViewById(R.id.reviewHeading);
        mReviewBody = rootView.findViewById(R.id.reviewEditText);
        mSubmitButton = rootView.findViewById(R.id.submitButton);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveReview(mUsername, mReviewBody.getText().toString(), mVendorName);
            }
        });

        return rootView;
    }

    private void saveReview(String mUsername, String s, String mVendorName) {

    }
}
