package nshmadhani.com.wakenbake.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nshmadhani.com.wakenbake.Models.Review;
import nshmadhani.com.wakenbake.R;

/**
 * Created by Nachiket on 27-Mar-18.
 */

public class ReviewFragmentListAdapter extends RecyclerView.Adapter<ReviewFragmentListAdapter.ListReviewHolder> {

    private List<Review> mReviewList;
    private Context context;

    public ReviewFragmentListAdapter(List<Review> mReviewList, Context context) {
        this.mReviewList = mReviewList;
        this.context = context;
    }

    @Override
    public ListReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout,parent, false);
        return new ListReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListReviewHolder holder, int position) {

        Review review = mReviewList.get(position);

        holder.mUsername.setText("Username: " + review.getmUsernameReview());

        holder.mReview.setText("Review: " + review.getmReview());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ListReviewHolder extends RecyclerView.ViewHolder {

        private TextView mUsername;
        private TextView mReview;

        public ListReviewHolder(View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.mRecyclerViewEmail);
            mReview = itemView.findViewById(R.id.mRecyclerViewReview);

        }
    }
}
