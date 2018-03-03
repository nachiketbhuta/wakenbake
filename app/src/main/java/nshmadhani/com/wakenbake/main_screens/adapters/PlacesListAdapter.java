package nshmadhani.com.wakenbake.main_screens.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.models.Places;

import static nshmadhani.com.wakenbake.main_screens.activities.LocationActivity.TAG;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ListViewHolder> {



    private List<Places> mData;

    public PlacesListAdapter(List<Places> data) {
        mData = data;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.list_places,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.bindView(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mImageView;


        public ListViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ListViewHolder: "+itemView);
            mTextView = itemView.findViewById(R.id.placeName);
            mImageView = itemView.findViewById(R.id.placeImage);
            itemView.setOnClickListener(this);
        }


        public void imagesFromFirebase(ImageView imageView) {
            mImageView = imageView;
        }

        public void bindView (Places places){
            //Name of the Places
            mTextView.setText(places.getName());
            //Images of the places
//            mImageView.setImageURI(Uri.parse(places.getImageUrl()));
        }

        @Override
        public void onClick(View view) {

        }
    }
}
