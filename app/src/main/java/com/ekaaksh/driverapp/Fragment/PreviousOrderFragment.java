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

import com.ekaaksh.driverapp.Activity.MainActivity;
import com.ekaaksh.driverapp.Adapters.PreviousOrderAdapter;
import com.ekaaksh.driverapp.Model.Beans.PreviousOrderBean;
import com.ekaaksh.driverapp.Model.Response.AcceptOrderResponse;
import com.ekaaksh.driverapp.Model.Response.OrderDetailResponse;
import com.ekaaksh.driverapp.Model.Response.PreviousOrderResponse;
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

public class PreviousOrderFragment extends Fragment {

    View rootView;
    String UserId;
    PreviousOrderAdapter previousOrderAdapter;
    PreviousOrderBean previousOrderBean;
    List<PreviousOrderBean> prevOrderList;
    RecyclerView rlPreviousOrder;
    Context context;
    public  static AVLoadingIndicatorView avi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.previous_order_layout, container, false);
        context = getActivity();
        prevOrderList = new ArrayList<>();

        UserId = AppPrefrences.getSavedUser(getActivity()).getId(); // Deliveryboy UserId.
        Log.e("UserId", ""+UserId);

        rlPreviousOrder = (RecyclerView) rootView.findViewById(R.id.rlPreviousOrder);
        avi = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Previous Order");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("July 19 - July 26");
        getPreviousOrdersApi();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }

    private void getPreviousOrdersApi() {
        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"deliverboy_id"},
                new String[]{UserId});
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Call<PreviousOrderResponse> call = apiInterface.PreviousOrder(builder.build());
            call.enqueue(new Callback<PreviousOrderResponse>() {
                @Override
                public void onResponse(Call<PreviousOrderResponse> call, Response<PreviousOrderResponse> response) {
                    // stopLoadingDialog();
                    avi.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null && response.body().getOrder() != null) {
                                prevOrderList.clear();

                                for (int i = 0; i < response.body().getOrder().size(); i++) {
                                    previousOrderBean = new PreviousOrderBean();
                                    previousOrderBean.setTotal_price(response.body().getOrder().get(i).getTotal_price());
                                    previousOrderBean.setStatus(response.body().getOrder().get(i).getStatus());
                                    previousOrderBean.setResta_name(response.body().getOrder().get(i).getResta_name());
                                    previousOrderBean.setOrder_id(response.body().getOrder().get(i).getOrder_id());

                                    prevOrderList.add(previousOrderBean);
                                }
                                //setting adapter
                                previousOrderAdapter = new PreviousOrderAdapter(context, prevOrderList,avi);
                                rlPreviousOrder.setHasFixedSize(true);
                                rlPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
                                rlPreviousOrder.setAdapter(previousOrderAdapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PreviousOrderResponse> call, Throwable t) {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "PreviousOrder Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
     }
}
