package nshmadhani.com.wakenbake.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.activities.PlaceActivity;
import nshmadhani.com.wakenbake.models.Places;

import static nshmadhani.com.wakenbake.activities.LocationActivity.TAG;

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

        final Places places = placesList.get(position);

        holder.mName.setText(places.getName());

        Picasso.with(context)
                .load(places.getImageUrl())
                .into(holder.mImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("placeLatitude", places.getLatitude());
                intent.putExtra("placeLongitude", places.getLongitude());
                intent.putExtra("placeName", places.getName());
                intent.putExtra("placeId", places.getPlaceId());
                intent.putExtra("placeRatings", places.getRatings());
                intent.putExtra("placeNumber", places.getPhoneNumber());
                intent.putExtra("placeUrl", places.getImageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public void updateList(List<Places> list){
        placesList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private ImageView mImage;
        private CardView cardView;

        private ViewHolder (View itemView) {
            super(itemView);
            Log.d(TAG, "ListViewHolder: "+itemView);
            mName = itemView.findViewById(R.id.placeName);
            mImage = itemView.findViewById(R.id.placeImage);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
