package nshmadhani.com.wakenbake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReviewResponse {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("review")
    @Expose
    private String review;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}