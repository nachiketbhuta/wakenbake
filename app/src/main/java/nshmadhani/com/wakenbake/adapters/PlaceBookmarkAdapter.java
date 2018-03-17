package nshmadhani.com.wakenbake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.models.PlaceBookmark;

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
        View view = li.inflate(R.layout.list_places,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlaceBookmark placeBookmark = list.get(position);

        holder.mName.setText(placeBookmark.getPlaceNAME());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private ImageView mImage;
        private LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.placeName);
            mImage = itemView.findViewById(R.id.placeImage);
            linearLayout = itemView.findViewById(R.id.placeLinearLayout);
        }
    }

}
