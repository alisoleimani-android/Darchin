package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.FunctionHelper;
import co.tinab.darchin.model.order.Order;
import co.tinab.darchin.model.order.OrderItem;
import co.tinab.darchin.view.toolbox.ButtonLight;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.MyRecyclerView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 2/13/2018.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    // interface:
    public interface Listener {
        void onBtnCommentClicked(Order order);
        void onBtnStatusClicked(Order order);
        void onGoingToStore(Order order);
    }
    private Listener listener;
    public void setOrderListAdapterListener(Listener listener){
        this.listener = listener;
    }

    private List<Order> orderList;

    public OrderListAdapter(List<Order> orderList){
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
        private ViewGroup containerCourier;
        private RoundedImageView img,imgCourier;
        private TextViewNormal txtName;
        private TextViewLight txtCreatedAt,txtStatus,txtDeliveryType,txtCourierName,txtVehicle,txtAddress;
        private MoneyTextView txtTotalPrice;
        private ButtonLight btnComment,btnShowMore;
        private TextViewLight txtDeliveryTime;
        private ExpandableLayout expandableLayout;

        private MyRecyclerView recyclerView;
        private List<OrderItem> orderItems = new ArrayList<>();
        private OrderItemListAdapter adapter;

        private View row;
        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            img = itemView.findViewById(R.id.img);
            imgCourier = itemView.findViewById(R.id.img_courier);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCreatedAt = itemView.findViewById(R.id.txt_created_at);
            txtDeliveryTime = itemView.findViewById(R.id.txt_delivery_time);
            txtTotalPrice = itemView.findViewById(R.id.txt_sum_total);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtDeliveryType = itemView.findViewById(R.id.txt_delivery_type);
            txtCourierName = itemView.findViewById(R.id.txt_courier_name);
            txtVehicle = itemView.findViewById(R.id.txt_vehicle);
            txtAddress = itemView.findViewById(R.id.txt_address);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnShowMore = itemView.findViewById(R.id.btn_show_more);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            containerCourier = itemView.findViewById(R.id.container_courier);

            recyclerView = itemView.findViewById(R.id.recycler_view);
            adapter = new OrderItemListAdapter(orderItems);
            recyclerView.setAdapter(adapter);

            txtStatus.setOnClickListener(this);
            btnShowMore.setOnClickListener(this);
            img.setOnClickListener(this);
            txtName.setOnClickListener(this);
            expandableLayout.setOnExpansionUpdateListener(this);
        }
        private void bind(Order order){
            if (order != null) {
                if (order.getStore() != null) {
                    //  set store name
                    txtName.setText(order.getStore().getName());

                    // store picture
                    if (FunctionHelper.isConnected(row.getContext())) {
                        Picasso.with(row.getContext())
                                .load(order.getStore().getPicture())
                                .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                                .centerCrop()
                                .error(R.drawable.ic_store_placeholder)
                                .placeholder(R.drawable.ic_store_placeholder)
                                .into(img);
                    }else {
                        Picasso.with(row.getContext())
                                .load(order.getStore().getPicture())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .resizeDimen(R.dimen.store_image_width,R.dimen.store_image_height)
                                .centerCrop()
                                .error(R.drawable.ic_store_placeholder)
                                .placeholder(R.drawable.ic_store_placeholder)
                                .into(img);
                    }
                }

                if (order.getCourier() != null) {
                    containerCourier.setVisibility(View.VISIBLE);

                    // courier picture
                    if (FunctionHelper.isConnected(row.getContext())) {
                        Picasso.with(row.getContext())
                                .load(order.getCourier().getPicture())
                                .resizeDimen(R.dimen.courier_image_width,R.dimen.courier_image_height)
                                .centerCrop()
                                .error(R.drawable.ic_placeholder_user_70dp)
                                .placeholder(R.drawable.ic_placeholder_user_70dp)
                                .into(imgCourier);
                    }else {
                        Picasso.with(row.getContext())
                                .load(order.getCourier().getPicture())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .resizeDimen(R.dimen.courier_image_width,R.dimen.courier_image_height)
                                .centerCrop()
                                .error(R.drawable.ic_placeholder_user_70dp)
                                .placeholder(R.drawable.ic_placeholder_user_70dp)
                                .into(imgCourier);
                    }
                    txtCourierName.setText(order.getCourier().getName());
                    txtVehicle.setText(order.getCourier().getVehicle());
                }else {
                    containerCourier.setVisibility(View.GONE);
                }

                txtCreatedAt.setText(order.getCreatedAt(row.getContext()));
                txtDeliveryTime.setText(order.getDeliveryTime(row.getContext()));
                txtDeliveryType.setText(order.getDeliverType(row.getContext()));
                txtTotalPrice.setText(order.getTotalPrice());

                // set address
                if (order.getDeliverType().equals("deliver")) {
                    txtAddress.setVisibility(View.VISIBLE);
                } else {
                    txtAddress.setVisibility(View.GONE);
                }
                txtAddress.setText(order.getAddressValue());

                // check order status
                if (order.getStatus().equals("cashier")) {
                    txtStatus.setBackgroundResource(R.drawable.btn_cashier_bk);
                    txtStatus.setTextColor(ContextCompat.getColor(row.getContext(),android.R.color.white));
                }else {
                    txtStatus.setBackgroundResource(R.drawable.order_frg_btn_bk);
                    txtStatus.setTextColor(ContextCompat.getColor(row.getContext(),R.color.colorAccent));
                }
                txtStatus.setText(order.getStatus(row.getContext()));

                if (order.getStatus().equals("sent")) {
                    btnComment.setVisibility(View.VISIBLE);
                    btnComment.setOnClickListener(this);

                    if (order.hasComment())
                        btnComment.setText(row.getContext().getString(R.string.view_comment));
                    else {
                        btnComment.setText(row.getContext().getString(R.string.send_comment));
                    }
                }else {
                    btnComment.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_comment:
                    if (listener != null) {
                        listener.onBtnCommentClicked(orderList.get(getAdapterPosition()));
                    }
                    break;

                case R.id.btn_show_more:
                    // notify adapter before toggle
                    if (!orderItems.isEmpty()) orderItems.clear();
                    orderItems.addAll(orderList.get(getAdapterPosition()).getOrderItems(row.getContext()));
                    adapter.notifyDataSetChanged();

                    // toggle expand layout
                    expandableLayout.toggle(true);
                    break;

                case R.id.txt_status:
                    if (listener != null) {
                        listener.onBtnStatusClicked(orderList.get(getAdapterPosition()));
                    }
                    break;

                case R.id.txt_name:
                    if (listener != null) {
                        listener.onGoingToStore(orderList.get(getAdapterPosition()));
                    }
                    break;

                case R.id.img:
                    if (listener != null) {
                        listener.onGoingToStore(orderList.get(getAdapterPosition()));
                    }
                    break;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            switch (state){
                case ExpandableLayout.State.COLLAPSED:
                    btnShowMore.setText(row.getContext().getString(R.string.btn_show_more));
                    break;

                case ExpandableLayout.State.EXPANDED:
                    btnShowMore.setText(row.getContext().getString(R.string.close_show_more));
                    break;
            }
        }
    }
}
