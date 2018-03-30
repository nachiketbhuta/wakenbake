package nshmadhani.com.wakenbake.Adapters;

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
import nshmadhani.com.wakenbake.Activities.FirebasePlaceActivity;
import nshmadhani.com.wakenbake.Models.FirebasePlaces;


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
        holder.mVendorName.setText(places.getmVendorName());

        if (Objects.equals(places.getmVendorUrl(), "")) {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(holder.mVendorImage);
        }
        else {
            Picasso.with(context)
                    .load(places.getmVendorUrl())
                    .into(holder.mVendorImage);
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
                intent.putExtra("vendor_url", places.getmVendorUrl());
                intent.putExtra("vendor_open", places.getmVendorOpenTime());
                intent.putExtra("vendor_close", places.getmVendorCloseTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFirebasePlacesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView mVendorImage;
        private TextView mVendorName;
        private CardView mCardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            mVendorImage = itemView.findViewById(R.id.placeImage);
            mVendorName = itemView.findViewById(R.id.placeName);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }
}
