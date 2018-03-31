package nshmadhani.com.wakenbake.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import nshmadhani.com.wakenbake.Models.PlaceBookmark;
import nshmadhani.com.wakenbake.R;

/**
 * Created by Nachiket on 17-Mar-18.
 */

public class PlaceBookmarkAdapter extends RecyclerView.Adapter<PlaceBookmarkAdapter.MyViewHolder> {

    private List<PlaceBookmark> list;
    private Context context;

    public PlaceBookmarkAdapter(List<PlaceBookmark> list, Context context) {
        this.list = list;
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
        PlaceBookmark placeBookmark = list.get(position);

        holder.mName.setText(placeBookmark.getPlaceNAME());

        Picasso.with(context)
                .load(placeBookmark.getPlaceURL())
                .into(holder.mImage);

        //holder.context.getApplicationContext();

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private ImageView mImage;
        private CardView mCardView;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.placeName);
            mImage = itemView.findViewById(R.id.placeImage);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }

}
