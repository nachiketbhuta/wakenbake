package nshmadhani.com.wakenbake.Holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nshmadhani.com.wakenbake.Models.TiffinPlaces;

public class TiffinPlacesHolder {

    @SerializedName("data")
    private List<TiffinPlaces> mTiffinPlaces;

    public List<TiffinPlaces> getmTiffinPlaces() {
        return mTiffinPlaces;
    }

    public void setmTiffinPlaces(List<TiffinPlaces> mTiffinPlaces) {
        this.mTiffinPlaces = mTiffinPlaces;
    }
}
