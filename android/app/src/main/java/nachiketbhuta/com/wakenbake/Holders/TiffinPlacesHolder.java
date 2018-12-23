package nachiketbhuta.com.wakenbake.Holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nachiketbhuta.com.wakenbake.Models.TiffinPlaces;

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
