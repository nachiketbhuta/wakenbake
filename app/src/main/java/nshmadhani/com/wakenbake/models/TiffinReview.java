package nshmadhani.com.wakenbake.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 23-Mar-18.
 */

public class TiffinReview {

    @SerializedName("usrname")
    private String mTiffinName;

    @SerializedName("review")
    private String mTiffinReview;

    @SerializedName("vendorname")
    private String mTiffinProvider;

    public TiffinReview(String mTiffinName, String mTiffinReview, String mTiffinProvider) {
        this.mTiffinName = mTiffinName;
        this.mTiffinReview = mTiffinReview;
        this.mTiffinProvider = mTiffinProvider;
    }

    public String getmTiffinName() {
        return mTiffinName;
    }

    public void setmTiffinName(String mTiffinName) {
        this.mTiffinName = mTiffinName;
    }

    public String getmTiffinReview() {
        return mTiffinReview;
    }

    public void setmTiffinReview(String mTiffinReview) {
        this.mTiffinReview = mTiffinReview;
    }

    public String getmTiffinProvider() {
        return mTiffinProvider;
    }

    public void setmTiffinProvider(String mTiffinProvider) {
        this.mTiffinProvider = mTiffinProvider;
    }
}
