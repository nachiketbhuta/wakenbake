package nshmadhani.com.wakenbake.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingsResponse {

    @SerializedName("ratings")
    @Expose
    private Double ratings;

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

}
