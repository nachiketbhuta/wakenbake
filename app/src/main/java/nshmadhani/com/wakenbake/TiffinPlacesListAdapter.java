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

public class TiffinPlacesListAdapter extends RecyclerView.Adapter
        <TiffinPlacesListAdapter.ListViewHolder>{

    private List<TiffinPlaces> mTiffinPlacesList;
    private Context context;

    public TiffinPlacesListAdapter(List<TiffinPlaces> tiffinPlacesList, Context context) {
        this.mTiffinPlacesList = tiffinPlacesList;
        this.context = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        TiffinPlaces mTiffinPlace = mTiffinPlacesList.get(position);

        holder.mTiffinName.setText(mTiffinPlace.getmTiffinName());

        Picasso.with(context)
                .load(R.drawable.no_image)
                .into(holder.mTiffinImage);
    }

    @Override
    public int getItemCount() {
        return mTiffinPlacesList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mTiffinImage;
        private TextView mTiffinName;
        private TextView mTimeStamp;
        private CardView mCardView;

        public ListViewHolder(View itemView) {
            super(itemView);

            mTiffinImage = itemView.findViewById(R.id.placeImage);
            mTiffinName = itemView.findViewById(R.id.placeName);
            mTimeStamp = itemView.findViewById(R.id.time_stamp);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }
}
