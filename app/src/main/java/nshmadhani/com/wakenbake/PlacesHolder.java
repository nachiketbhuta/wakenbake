package nshmadhani.com.wakenbake;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nachiket on 10-Mar-18.
 */

public class PlacesHolder {
    @SerializedName("data")
    private List<Places> mPlaces;

    public List<Places> getmPlaces() {
        return mPlaces;
    }

    public void setmPlaces(List<Places> mPlaces) {
        this.mPlaces = mPlaces;
    }
}
