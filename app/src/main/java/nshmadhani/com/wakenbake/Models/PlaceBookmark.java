package nshmadhani.com.wakenbake.Models;

import android.content.Context;

import com.orm.SugarRecord;

public class PlaceBookmark extends SugarRecord {

    private String placeNAME;
    private String placeURL;

    public PlaceBookmark () {}

    public PlaceBookmark(String placeNAME, String placeURL) {
        this.placeNAME = placeNAME;
        this.placeURL = placeURL;
    }

    public PlaceBookmark(String placeNAME) {
        this.placeNAME = placeNAME;
    }

    public String getPlaceNAME() {
        return placeNAME;
    }

    public void setPlaceNAME(String placeNAME) {
        this.placeNAME = placeNAME;
    }

    public String getPlaceURL() {
        return placeURL;
    }

    public void setPlaceURL(String placeURL) {
        this.placeURL = placeURL;
    }
}
