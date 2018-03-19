package nshmadhani.com.wakenbake;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PlacesHolder {

    @SerializedName("data")
    private List<FirebasePlaces> mPlaces;

    @SerializedName("data")
    private List<TiffinPlaces> mTiffinPlaces;

    public List<TiffinPlaces> getmTiffinPlaces() {
        return mTiffinPlaces;
    }

    public void setmTiffinPlaces(List<TiffinPlaces> mTiffinPlaces) {
        this.mTiffinPlaces = mTiffinPlaces;
    }

    public List<FirebasePlaces> getmPlaces() {
        return mPlaces;
    }

    public void setmPlaces(List<FirebasePlaces> mPlaces) {
        this.mPlaces = mPlaces;
    }
}
