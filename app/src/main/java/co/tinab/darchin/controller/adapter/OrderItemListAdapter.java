package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.order.OrderItem;
import co.tinab.darchin.view.toolbox.TextViewLight;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {
    private List<OrderItem> orderItems;

    OrderItemListAdapter(List<OrderItem> orderItems){
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewLight txtName,txtPrice;

        ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }

        private void bind(OrderItem orderItem){
            if (orderItem != null) {
                txtName.setText(orderItem.getName());
                if (orderItem.getCount() > 1) {
                    txtPrice.setText(orderItem.getPrice().concat(" x ").concat(String.valueOf(orderItem.getCount())));
                }else{
                    txtPrice.setText(orderItem.getPrice());
                }
            }
        }
    }
}
