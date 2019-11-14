package com.ekaaksh.driverapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ekaaksh.driverapp.Model.Beans.ReviewBean;
import com.ekaaksh.driverapp.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private Context context;
    private List<ReviewBean> reviewList;

    public ReviewsAdapter(Context context, List<ReviewBean> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_user_feedback, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @NonNull


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvReviewerName.setText(""+reviewList.get(position).getUsername());
        holder.tvReviewDate.setText(""+reviewList.get(position).getCreated_at());
        holder.tvStarRating.setText("| "+reviewList.get(position).getRatting() + " stars");
        holder.tvReview.setText(""+reviewList.get(position).getReview_text());

        String imageName = ""+reviewList.get(position).getImage();
        String imageUrl = "https://www.ekaakshgroup.in/FoodDeliverySystem/food/"+imageName;


        Picasso.with(context).load(imageUrl).fit().centerCrop()
               .placeholder(R.drawable.profile)
               .error(R.drawable.profile)
               .into(holder.ivReviewerImage);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvStarRating,tvReviewerName,tvReview,tvReviewDate;
        public CircularImageView ivReviewerImage;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tvStarRating = (TextView) itemView.findViewById(R.id.tvStarRating);
            this.tvReviewerName = (TextView) itemView.findViewById(R.id.tvReviewerName);
            this.tvReview = (TextView) itemView.findViewById(R.id.tvReview);
            this.tvReviewDate = (TextView) itemView.findViewById(R.id.tvReviewDate);
            this.ivReviewerImage = (CircularImageView) itemView.findViewById(R.id.ivReviewerImage);
        }
    }
}
