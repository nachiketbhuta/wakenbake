package nshmadhani.com.wakenbake.Models;

import com.orm.SugarRecord;

public class PlaceBookmark extends SugarRecord {

    private String placeID;
    private String placeNAME;
    private String placeURL;

    public PlaceBookmark () {}

    public PlaceBookmark(String placeID, String placeNAME, String placeURL) {
        this.placeID = placeID;
        this.placeNAME = placeNAME;
        this.placeURL = placeURL;
    }

    public PlaceBookmark(String placeNAME) {
        this.placeNAME = placeNAME;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
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
