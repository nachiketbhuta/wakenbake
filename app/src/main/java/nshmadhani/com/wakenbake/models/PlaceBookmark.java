package nshmadhani.com.wakenbake.models;

import com.orm.SugarRecord;

/**
 * Created by Nachiket on 17-Mar-18.
 */

public class PlaceBookmark extends SugarRecord {

    private String placeID;
    private String placeNAME;

    public PlaceBookmark () {}

    public PlaceBookmark(String placeID, String placeNAME) {
        this.placeID = placeID;
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
}
