package com.ekaaksh.driverapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ekaaksh.driverapp.Model.Beans.PreviousOrderBean;
import com.ekaaksh.driverapp.Model.Response.OrderDetailResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>{

    private Context context;
    private List<PreviousOrderBean> prevOrderList;
    AVLoadingIndicatorView avi;

    public PreviousOrderAdapter(Context context, List<PreviousOrderBean> prevOrderList, AVLoadingIndicatorView avi) {
        this.context = context;
        this.prevOrderList = prevOrderList;
        this.avi = avi;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_previous_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @NonNull


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvShopName.setText(prevOrderList.get(position).getResta_name());
        holder.tvAmount.setText("₹"+prevOrderList.get(position).getTotal_price());

        if((prevOrderList.get(position).getStatus()!=null)){
            if(prevOrderList.get(position).getStatus().equals("4")) {
                holder.tvOrderStatus.setText("Delivered");
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.greenn));
            }
            else if(prevOrderList.get(position).getStatus().equals("5")) {
                holder.tvOrderStatus.setText("Cancelled");
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.red));
            }
        }

        holder.rlRowPreviousOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prevOrderList.get(position).getOrder_id()!=null)
                    getOrderDetailApi(prevOrderList.get(position).getOrder_id(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prevOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvShopName,tvTime,tvAmount,tvOrderStatus;
        public RelativeLayout rlRowPreviousOrder;
        public AVLoadingIndicatorView avi;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tvShopName = (TextView) itemView.findViewById(R.id.tvShopName);
            this.tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            this.tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            this.tvOrderStatus = (TextView) itemView.findViewById(R.id.tvOrderStatus);
            this.rlRowPreviousOrder = (RelativeLayout) itemView.findViewById(R.id.rlRowPreviousOrder);
            this.avi = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
        }
    }

    private void getOrderDetailApi(String orderId, final int position) {
        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"order_id"},
                new String[]{orderId});

        if(CommonUtils.isNetworkAvailable(context)){
            Call<OrderDetailResponse> call = apiInterface.OrderDetail(builder.build());
            call.enqueue(new Callback<OrderDetailResponse>() {
                @Override
                public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                    avi.setVisibility(View.GONE);
                    Log.d("11111", "11111");
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null && response.body().getOrder_details() != null)
                                showOrderDetailDialog(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                    //onApiFailure(call, t);
                    avi.setVisibility(View.GONE);
                    Toast.makeText(context, "Something went wrong!.", Toast.LENGTH_SHORT).show();
                    }
                });
        }else {
            avi.setVisibility(View.GONE);
            Toast.makeText(context, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrderDetailDialog(OrderDetailResponse response) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detail_order_layout);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // set the custom dialog components - text, image and button
        TextView tvOrderStaus = (TextView) dialog.findViewById(R.id.tvOrderStaus);
        TextView tvOrderDate = (TextView) dialog.findViewById(R.id.tvOrderDate);
        RecyclerView recyclerItemDetail = (RecyclerView) dialog.findViewById(R.id.recyclerItemDetail);

        if(response.getOrder_details().getOrder_status().equals("4")) {
            tvOrderStaus.setText("Delivered");
            tvOrderDate.setText(CommonUtils.convertDate(""+response.getOrder_details().getDelivered_date_time()));
        }
        else if(response.getOrder_details().getOrder_status().equals("5")) {
            tvOrderStaus.setText("Cancelled");
            tvOrderDate.setText(CommonUtils.convertDate(""+response.getOrder_details().getReject_date_time()));
        }

        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(context,response.getOrder());
        recyclerItemDetail.setHasFixedSize(true);
        recyclerItemDetail.setLayoutManager(new LinearLayoutManager(context));
        recyclerItemDetail.setAdapter(detailOrderAdapter);

        dialog.show();
    }

}
