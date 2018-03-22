package nshmadhani.com.wakenbake.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.activities.FirebasePlaceActivity;
import nshmadhani.com.wakenbake.models.FirebasePlaces;


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

        final FirebasePlaces places = mFirebasePlacesList.get(position);
        holder.mPlaceName.setText(places.getmVendorName());


        if (Objects.equals(places.getmVendorUrl(), "")) {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(holder.mPlaceImage);
        }
        else {
            Picasso.with(context)
                    .load(places.getmVendorUrl())
                    .into(holder.mPlaceImage);
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FirebasePlaceActivity.class);
                intent.putExtra("vendor_name", places.getmVendorName());
                intent.putExtra("vendor_ratings", places.getmVendorRatings());
                intent.putExtra("vendor_phone", places.getmVendorPhoneNumber());
                intent.putExtra("vendor_id", places.getmVendorId());
                intent.putExtra("vendor_lat", places.getmVendorLatitude());
                intent.putExtra("vendor_lng", places.getmVendorLongitude());
                intent.putExtra("vendor_food", places.getmVendorFoodItems());
                context.startActivity(intent);
            }
        });
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
