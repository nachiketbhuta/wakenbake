package nshmadhani.com.wakenbake.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import nshmadhani.com.wakenbake.Adapters.PlaceBookmarkAdapter;
import nshmadhani.com.wakenbake.Models.PlaceBookmark;
import nshmadhani.com.wakenbake.Models.WakeNBake;
import nshmadhani.com.wakenbake.R;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceBookmarkAdapter adapter;
    private List<PlaceBookmark> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        recyclerView  = findViewById(R.id.bookmarkRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = WakeNBake.database.iDoa().fetchAllPlaces();

        for (PlaceBookmark p : list) {

            p.setPlaceNAME(p.getPlaceNAME());
            p.setPlaceURL(p.getPlaceURL());

        }

        adapter = new PlaceBookmarkAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
