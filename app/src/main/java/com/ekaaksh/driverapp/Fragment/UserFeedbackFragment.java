package com.ekaaksh.driverapp.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ekaaksh.driverapp.Adapters.ReviewsAdapter;
import com.ekaaksh.driverapp.Model.Beans.ReviewBean;
import com.ekaaksh.driverapp.Model.Response.ReviewsResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFeedbackFragment extends Fragment {

    View rootView;
    RecyclerView recyclerReviews;
    AVLoadingIndicatorView avi;
    String UserId;
    Context context;
    List<ReviewBean> ReviewList;
    ReviewsAdapter reviewsAdapter;
    ReviewBean reviewBean;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_feedbak_layout, container, false);
        context = getActivity();

        UserId = AppPrefrences.getSavedUser(getActivity()).getId(); // Deliveryboy UserId.
        Log.e("UserId", ""+UserId);

        ReviewList = new ArrayList<>();

        recyclerReviews = (RecyclerView)rootView.findViewById(R.id.recyclerReviews);
        avi = (AVLoadingIndicatorView)rootView.findViewById(R.id.avi);

        getReviewsApi();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("User FeedBack");

        getReviewsApi();
    }
    private void getReviewsApi() {
        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"deliveryboy_id"},
                new String[]{UserId});
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Call<ReviewsResponse> call = apiInterface.Reviews(builder.build());
            call.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                    avi.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                            if (response.body()!= null) {
                                ReviewList.clear();

                                for(int i =0; i<response.body().getReview().size();i++){
                                    reviewBean = new ReviewBean();
                                    reviewBean.setUsername(""+response.body().getReview().get(i).getUsername());
                                    reviewBean.setId(""+response.body().getReview().get(i).getId());
                                    reviewBean.setImage(""+response.body().getReview().get(i).getImage());
                                    reviewBean.setCreated_at(""+response.body().getReview().get(i).getCreated_at());
                                    reviewBean.setRatting(""+response.body().getReview().get(i).getRatting());
                                    reviewBean.setReview_text(""+response.body().getReview().get(i).getReview_text());

                                    ReviewList.add(reviewBean);

                                }
                                //setting adapter
                                reviewsAdapter = new ReviewsAdapter(context, ReviewList);
                                recyclerReviews.setHasFixedSize(true);
                                recyclerReviews.setLayoutManager(new LinearLayoutManager(context));
                                recyclerReviews.setAdapter(reviewsAdapter);

                            }
                      }
                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Reviews Response Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

}
