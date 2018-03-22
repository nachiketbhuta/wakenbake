package nshmadhani.com.wakenbake.holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nshmadhani.com.wakenbake.models.TiffinPlaces;

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
