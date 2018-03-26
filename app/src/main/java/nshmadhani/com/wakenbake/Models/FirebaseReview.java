package nshmadhani.com.wakenbake.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 26-Mar-18.
 */

public class FirebaseReview {

    @SerializedName("usrname")
    private String mUsername;

    @SerializedName("review")
    private String mUserReview;

    @SerializedName("vendorname")
    private String mVendorName;

    public FirebaseReview(String mUsername, String mUserReview, String mVendorName) {
        this.mUsername = mUsername;
        this.mUserReview = mUserReview;
        this.mVendorName = mVendorName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmUserReview() {
        return mUserReview;
    }

    public void setmUserReview(String mUserReview) {
        this.mUserReview = mUserReview;
    }

    public String getmVendorName() {
        return mVendorName;
    }

    public void setmVendorName(String mVendorName) {
        this.mVendorName = mVendorName;
    }
}
