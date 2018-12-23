package nachiketbhuta.com.wakenbake.Abstracts;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import nachiketbhuta.com.wakenbake.Interfaces.IDoa;
import nachiketbhuta.com.wakenbake.Models.PlaceBookmark;


@Database(entities = {PlaceBookmark.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract IDoa iDoa();
}
