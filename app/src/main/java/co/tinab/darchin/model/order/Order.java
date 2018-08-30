package co.tinab.darchin.model.order;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.tinab.darchin.R;
import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.model.User;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 2/13/2018.
 */

public class Order implements Parcelable {
    @Expose
    @SerializedName("id")
    private int orderId;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("user")
    private User user;

    @Expose
    @SerializedName("store")
    private Store store;

    @Expose
    @SerializedName("deliver_type")
    private String deliverType;

    @Expose
    @SerializedName("deliver_time")
    private String deliveryTime;

    @Expose
    @SerializedName("total_price")
    private String totalPrice;

    @Expose
    @SerializedName("paid_money")
    private String paidMoney;

    @Expose
    @SerializedName("courier")
    private Courier courier;

    @Expose
    @SerializedName("coupon")
    private Coupon coupon;

    @Expose
    @SerializedName("address_id")
    private int addressId;

    @Expose
    @SerializedName("address_value")
    private String addressValue;

    @Expose
    @SerializedName("vote")
    private Vote vote;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("items")
    private List<ProductItem> items = new ArrayList<>();

    @Expose
    @SerializedName("coupon_price")
    private String couponPrice;

    @Expose
    @SerializedName("discount_price")
    private String discountPrice;

    @Expose
    @SerializedName("courier_price")
    private String courierPrice;

    private Order(Parcel in) {
        orderId = in.readInt();
        type = in.readString();
        status = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        store = in.readParcelable(Store.class.getClassLoader());
        deliverType = in.readString();
        deliveryTime = in.readString();
        totalPrice = in.readString();
        paidMoney = in.readString();
        courier = in.readParcelable(Courier.class.getClassLoader());
        coupon = in.readParcelable(Coupon.class.getClassLoader());
        addressId = in.readInt();
        addressValue = in.readString();
        vote = in.readParcelable(Vote.class.getClassLoader());
        createdAt = in.readString();
        in.readTypedList(items, ProductItem.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public boolean hasComment() {
        return vote != null && vote.comment != null;
    }

    public String getCreatedAt(Context context) {
        if (createdAt != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                return MyDateTime.convertGeorgianToShamsi(createdAt);
            }
            return createdAt;
        }else {
            return context.getString(R.string.not_registered);
        }
    }

    public User getUser() {
        return user;
    }

    public Store getStore() {
        return store;
    }

    public String getType() {
        if (type == null) {
            return "";
        }
        return type;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public List<ProductItem> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public List<OrderItem> getOrderItems(Context context){
        long tax = 0;
        long packingPrice = 0;
        long discount = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (ProductItem item : items){
            orderItems.add(new OrderItem(item.getName(),item.getPrice(),item.getCount()));
            tax += item.getTaxPrice();
            packingPrice += item.getPackingPrice();
        }

        if (couponPrice != null && !couponPrice.isEmpty()) {
            discount += Long.parseLong(couponPrice);
        }

        if (discountPrice != null && !discountPrice.isEmpty()) {
            discount += Long.parseLong(discountPrice);
        }

        orderItems.add(new OrderItem(context.getString(R.string.discount), String.valueOf(discount), 0));
        orderItems.add(new OrderItem(context.getString(R.string.tax),String.valueOf(tax),0));
        orderItems.add(new OrderItem(context.getString(R.string.packing),String.valueOf(packingPrice),0));

        if (courierPrice != null && !courierPrice.isEmpty()) {
            orderItems.add(new OrderItem(context.getString(R.string.courier_price),courierPrice,0));
        }
        return orderItems;
    }

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public String getStatus(Context context) {
        String status = "";
        switch (this.status){
            case "wait":
                status = context.getString(R.string.order_status_wait);
                break;

            case "cashier":
                status = context.getString(R.string.order_status_cashier);
                break;

            case "product":
                status = context.getString(R.string.order_status_product);
                break;

            case "courier":
                status = context.getString(R.string.order_status_courier);
                break;

            case "sent":
                status = context.getString(R.string.order_status_sent);
                break;

            case "cancel":
                status = context.getString(R.string.order_status_cancel);
                break;
        }
        return status;
    }

    public Courier getCourier() {
        return courier;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getAddressValue() {
        if (addressValue == null) {
            return "";
        }
        return addressValue;
    }

    public String getDeliverType() {
        if (deliverType == null) {
            return "";
        }
        return deliverType;
    }

    public String getDeliverType(Context context) {
        String deliveryType = "";
        if (deliverType != null) {
            if (deliverType.equals("deliver")) {
                deliveryType = context.getString(R.string.delivery_type_courier);
            }

            if (deliverType.equals("inside")) {
                deliveryType = context.getString(R.string.delivery_type_inside);
            }
        }
        return deliveryType;
    }

    public String getDeliveryTime() {
        if (deliveryTime == null) {
            return "";
        }
        return deliveryTime;
    }

    public String getDeliveryTime(Context context){
        if (deliveryTime != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                return MyDateTime.convertGeorgianToShamsi(deliveryTime);
            }
            return deliveryTime;

        }else {
            return context.getString(R.string.not_registered);
        }
    }

    public String getPaidMoney() {
        if (paidMoney == null) {
            return "";
        }
        return paidMoney;
    }

    public String getTotalPrice() {
        if (totalPrice == null) {
            return "";
        }
        return totalPrice;
    }

    public Vote getVote() {
        if (vote == null) {
            return new Vote();
        }
        return vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(store, flags);
        dest.writeString(deliverType);
        dest.writeString(deliveryTime);
        dest.writeString(totalPrice);
        dest.writeString(paidMoney);
        dest.writeParcelable(courier, flags);
        dest.writeParcelable(coupon, flags);
        dest.writeInt(addressId);
        dest.writeString(addressValue);
        dest.writeParcelable(vote, flags);
        dest.writeString(createdAt);
        dest.writeTypedList(items);
    }
}
