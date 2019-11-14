package com.ekaaksh.driverapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ekaaksh.driverapp.Model.Response.GetProfileResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRevenueFragment extends Fragment {

    View rootView;
    String UserId;
    AVLoadingIndicatorView avi;
    TextView tvTotalOrders, tvCompletedOrders,tvCancelledOrders,tvTotalRevenueAmount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manage_revenue_layout, container, false);

        UserId = AppPrefrences.getSavedUser(getActivity()).getId(); // Deliveryboy UserId.
        Log.e("UserId", ""+UserId);

        tvTotalOrders = (TextView)rootView.findViewById(R.id.tvTotalOrders);
        tvCompletedOrders = (TextView)rootView.findViewById(R.id.tvCompletedOrders);
        tvCancelledOrders = (TextView)rootView.findViewById(R.id.tvCancelledOrders);
        tvTotalRevenueAmount = (TextView)rootView.findViewById(R.id.tvTotalRevenueAmount);
        avi = (AVLoadingIndicatorView)rootView.findViewById(R.id.avi);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Manage Revenue");
        getOrderRevenueDetailsApi();
    }

    private void getOrderRevenueDetailsApi() {

        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"deliverboy_id"},
                new String[]{UserId});
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Call<GetProfileResponse> call = apiInterface.GetProfileDetails(builder.build());
            call.enqueue(new Callback<GetProfileResponse>() {
                @Override
                public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

                    avi.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null) {
                                tvTotalOrders.setText(""+response.body().getTotal_order());
                                tvCompletedOrders.setText(""+response.body().getComplete_order());
                                tvCancelledOrders.setText(""+response.body().getCancel_order());
                                tvTotalRevenueAmount.setText("₹"+response.body().getRevenue());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Revenue Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
