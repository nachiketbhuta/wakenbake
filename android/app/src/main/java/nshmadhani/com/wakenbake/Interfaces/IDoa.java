package nshmadhani.com.wakenbake.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nshmadhani.com.wakenbake.Models.PlaceBookmark;

@Dao
public interface IDoa {

    @Insert
    void addPlace(PlaceBookmark placeBookmark);

    @Query("SELECT * FROM place_bookmark")
    List<PlaceBookmark> fetchAllPlaces();

    @Query("SELECT name FROM place_bookmark WHERE id =:id")
    String checkInDatabase(String id);

}
