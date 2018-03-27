package nshmadhani.com.wakenbake.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orm.SugarContext;

import java.util.List;

import nshmadhani.com.wakenbake.Adapters.PlaceBookmarkAdapter;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.Models.PlaceBookmark;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceBookmarkAdapter adapter;
    private List<PlaceBookmark> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        SugarContext.init(this);

        recyclerView  = findViewById(R.id.bookmarkRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = PlaceBookmark.listAll(PlaceBookmark.class);

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