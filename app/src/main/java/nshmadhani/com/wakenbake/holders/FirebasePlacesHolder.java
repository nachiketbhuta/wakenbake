package nshmadhani.com.wakenbake.holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nshmadhani.com.wakenbake.models.FirebasePlaces;


public class FirebasePlacesHolder {

    @SerializedName("data")
    private List<FirebasePlaces> mPlaces;

    public List<FirebasePlaces> getmPlaces() {
        return mPlaces;
    }

    public void setmPlaces(List<FirebasePlaces> mPlaces) {
        this.mPlaces = mPlaces;
    }
}
