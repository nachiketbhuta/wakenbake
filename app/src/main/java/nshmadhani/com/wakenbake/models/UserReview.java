package nshmadhani.com.wakenbake.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 21-Mar-18.
 */

public class UserReview {

    @SerializedName("usrname")
    private String mUsername;

    @SerializedName("review")
    private String mReview;

    @SerializedName("vendorname")
    private String mVendorName;

    public UserReview(String mUsername, String mReview, String mVendorName) {
        this.mUsername = mUsername;
        this.mReview = mReview;
        this.mVendorName = mVendorName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }

    public String getmVendorName() {
        return mVendorName;
    }

    public void setmVendorName(String mVendorName) {
        this.mVendorName = mVendorName;
    }
}
