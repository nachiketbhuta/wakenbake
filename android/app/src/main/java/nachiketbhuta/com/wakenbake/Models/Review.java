package nachiketbhuta.com.wakenbake.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 23-Mar-18.
 */

public class Review {

    @SerializedName("username")
    private String mUsernameReview;

    @SerializedName("review")
    private String mReview;


    public String getmUsernameReview() {
        return mUsernameReview;
    }

    public void setmUsernameReview(String mUsernameReview) {
        this.mUsernameReview = mUsernameReview;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }
}
