package nshmadhani.com.wakenbake.Holders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingsResponse {

    @SerializedName("ratings")
    @Expose
    private List<Double> ratings = null;

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

}