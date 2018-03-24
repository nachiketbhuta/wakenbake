package nshmadhani.com.wakenbake.models;

import java.util.List;

/**
 * Created by Nachiket on 24-Mar-18.
 */

public class MasterData {
    List<GooglePlaces> day;
    List<FirebasePlaces> night;
    List<TiffinPlaces> tiffin;

    public void setDay(List<GooglePlaces> day) {
        this.day = day;
    }

    public void setNight(List<FirebasePlaces> night) {
        this.night = night;
    }

    public void setTiffin(List<TiffinPlaces> tiffin) {
        this.tiffin = tiffin;
    }

    public List<GooglePlaces> getDay() {
        return day;
    }

    public List<FirebasePlaces> getNight() {
        return night;
    }

    public List<TiffinPlaces> getTiffin() {
        return tiffin;
    }
}
