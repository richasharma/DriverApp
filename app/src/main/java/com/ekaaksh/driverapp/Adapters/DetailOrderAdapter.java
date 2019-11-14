package com.ekaaksh.driverapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ekaaksh.driverapp.Model.Beans.OrderBean;
import com.ekaaksh.driverapp.R;

import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderBean> orderBeanList;

    public DetailOrderAdapter(Context context, List<OrderBean> orderBeanList) {
        this.context = context;
        this.orderBeanList = orderBeanList;
    }

    @Override
    public DetailOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_detail_order, parent, false);
        DetailOrderAdapter.ViewHolder viewHolder = new DetailOrderAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @NonNull
    @Override
    public void onBindViewHolder(DetailOrderAdapter.ViewHolder holder, final int position) {

        holder.tvItemName.setText(orderBeanList.get(position).getMenu_name());
        holder.tvItemQuantity.setText( orderBeanList.get(position).getItemQty());
        holder.tvItemPrice.setText("₹" + orderBeanList.get(position).getItemAmt());
    }

    @Override
    public int getItemCount() {
        return orderBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItemName, tvItemQuantity, tvItemPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            this.tvItemQuantity = (TextView) itemView.findViewById(R.id.tvItemQuantity);
            this.tvItemPrice = (TextView) itemView.findViewById(R.id.tvItemPrice);
        }
    }
}
