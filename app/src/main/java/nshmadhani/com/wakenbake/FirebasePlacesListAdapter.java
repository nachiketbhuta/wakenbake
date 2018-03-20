package nshmadhani.com.wakenbake;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nachiket on 19-Mar-18.
 */

public class FirebasePlacesListAdapter extends RecyclerView.Adapter
        <FirebasePlacesListAdapter.MyViewHolder> {

    private List<FirebasePlaces> mFirebasePlacesList;
    private Context context;

    public FirebasePlacesListAdapter(List<FirebasePlaces> mFirebasePlacesList, Context context) {
        this.mFirebasePlacesList = mFirebasePlacesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.place_card,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FirebasePlaces places = mFirebasePlacesList.get(position);
        holder.mPlaceName.setText(places.getmVendorName());
        Picasso.with(context)
                .load(R.drawable.no_image)
                .into(holder.mPlaceImage);
    }

    @Override
    public int getItemCount() {
        return mFirebasePlacesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView mPlaceImage;
        private TextView mPlaceName;
        private TextView mPlaceTime;
        private CardView mCardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mPlaceImage = itemView.findViewById(R.id.placeImage);
            mPlaceName = itemView.findViewById(R.id.placeName);
            mPlaceTime = itemView.findViewById(R.id.time_stamp);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }
}
