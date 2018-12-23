package nachiketbhuta.com.wakenbake.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "place_bookmark")
public class PlaceBookmark {

    public PlaceBookmark(@NonNull String placeID, String placeNAME, String placeURL) {
        this.placeID = placeID;
        this.placeNAME = placeNAME;
        this.placeURL = placeURL;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String placeID;

    @ColumnInfo(name = "name")
    private String placeNAME;

    @ColumnInfo(name = "image_url")
    private String placeURL;

    @NonNull
    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(@NonNull String placeID) {
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
