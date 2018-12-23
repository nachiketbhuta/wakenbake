package nachiketbhuta.com.wakenbake.Adapters;

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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import nachiketbhuta.com.wakenbake.R;
import nachiketbhuta.com.wakenbake.Activities.GooglePlacesActivity;
import nachiketbhuta.com.wakenbake.Models.GooglePlaces;

import static nachiketbhuta.com.wakenbake.Activities.LocationActivity.TAG;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class GooglePlacesListAdapter extends RecyclerView.Adapter<GooglePlacesListAdapter.ViewHolder> {

    private List<GooglePlaces> googlePlacesList;
    private Context context;

    public GooglePlacesListAdapter(List<GooglePlaces> googlePlacesList, Context context) {
        this.googlePlacesList = googlePlacesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.place_card,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final GooglePlaces googlePlaces = googlePlacesList.get(position);

        holder.mName.setText(googlePlaces.getName());

        if (Objects.equals(googlePlaces.getImageUrl(), "")) {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(holder.mImage);
        }
        else {
            Picasso.with(context)
                    .load(googlePlaces.getImageUrl())
                    .into(holder.mImage);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("placeLatitude", googlePlaces.getLatitude());
                intent.putExtra("placeLongitude", googlePlaces.getLongitude());
                intent.putExtra("placeName", googlePlaces.getName());
                intent.putExtra("placeId", googlePlaces.getPlaceId());
                intent.putExtra("placeRatings", googlePlaces.getRatings());
                intent.putExtra("placeUrl", googlePlaces.getImageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return googlePlacesList.size();
    }

    public void updateList(List<GooglePlaces> list){
        googlePlacesList = list;
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
