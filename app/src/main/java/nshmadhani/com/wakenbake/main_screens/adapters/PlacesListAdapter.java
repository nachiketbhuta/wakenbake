package nshmadhani.com.wakenbake.main_screens.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.models.Places;

import static nshmadhani.com.wakenbake.main_screens.activities.LocationActivity.TAG;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private List<Places> placesList;
    private Context context;

    public PlacesListAdapter(List<Places> placesList, Context context) {
        this.placesList = placesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.list_places,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Places places = placesList.get(position);
        holder.mName.setText(places.getName());
        Picasso.with(context)
                .load(places.getImageUrl())
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private ImageView mImage;

        public ViewHolder (View itemView) {
            super(itemView);
            Log.d(TAG, "ListViewHolder: "+itemView);
            mName = itemView.findViewById(R.id.placeName);
            mImage = itemView.findViewById(R.id.placeImage);

        }
    }
}
