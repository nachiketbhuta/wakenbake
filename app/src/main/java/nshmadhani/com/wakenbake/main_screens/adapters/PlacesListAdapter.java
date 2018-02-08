package nshmadhani.com.wakenbake.main_screens.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.classes.Places;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class PlacesListAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_places, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mImageView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.placeText);
            mImageView = itemView.findViewById(R.id.placeImage);
            itemView.setOnClickListener(this);
        }

        public void bindView (int position) {

            Places places = new Places();
            //Name of the Places
            mTextView.setText(places.getName());
            //Images of the places
            //mImageView.set
        }

        @Override
        public void onClick(View view) {

        }
    }
}
