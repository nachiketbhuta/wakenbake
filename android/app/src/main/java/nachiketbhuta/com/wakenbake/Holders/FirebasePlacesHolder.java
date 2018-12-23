package nachiketbhuta.com.wakenbake.Holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nachiketbhuta.com.wakenbake.Models.FirebasePlaces;


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
