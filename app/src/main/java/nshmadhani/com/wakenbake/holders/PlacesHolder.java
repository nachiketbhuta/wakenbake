package nshmadhani.com.wakenbake.holders;

import android.content.Context;

import java.util.List;

import nshmadhani.com.wakenbake.models.GooglePlaces;
import nshmadhani.com.wakenbake.models.Places;

/**
 * Created by Nachiket on 22-Mar-18.
 */

public class PlacesHolder {

    public Places places;
    public Context context;
    public List<Places> mPlacesList;

    public void updateList(List<Places> list) {
            mPlacesList = list;
           // notifyDataSetChanged();
    }

}
