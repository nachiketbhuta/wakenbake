package nshmadhani.com.wakenbake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.models.Places;

/**
 * Created by Nachiket on 22-Mar-18.
 */

public class MasterPlacesListAdapter extends RecyclerView.Adapter<MasterPlacesListAdapter.ListViewHolder> {

    private List<Places> mList;
    private Context context;

    public MasterPlacesListAdapter(List<Places> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_places,parent ,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        Places places = mList.get(position);

        holder.mPlaceName.setText(places.getName());

        Picasso.with(context)
                .load(R.drawable.no_image)
                .into(holder.mPlaceImage);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView mPlaceName;
        private ImageView mPlaceImage;

        ListViewHolder(View itemView) {
            super(itemView);

            mPlaceName = itemView.findViewById(R.id.listPlaceName);
            mPlaceImage = itemView.findViewById(R.id.listPlaceImage);

        }
    }

}
