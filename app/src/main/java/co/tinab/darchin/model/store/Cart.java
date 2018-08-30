package co.tinab.darchin.model.store;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.address.Area;

/**
 * Created by A.S.R on 4/19/2018.
 */

public class Cart implements Parcelable{
    private List<ProductItem> items = new ArrayList<>();
    private Period selectedPeriod;
    private Address selectedAddress;
    private String description;
    private String deliverType;
    private String couponCode;
    private Store store;
    private List<Product> products = new ArrayList<>();

    private Cart(){}

    private Cart(Parcel in) {
        items = in.createTypedArrayList(ProductItem.CREATOR);
        selectedPeriod = in.readParcelable(Period.class.getClassLoader());
        selectedAddress = in.readParcelable(Address.class.getClassLoader());
        store = in.readParcelable(Store.class.getClassLoader());
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public static Cart newInstance(Store store){
        Cart cart = new Cart();
        cart.store = store;
        return cart;
    }

    public Store getStore() {
        return store;
    }

    public String getDescription() {
        return description;
    }

    public String getDeliverType() {
        return deliverType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Period getSelectedPeriod() {
        return selectedPeriod;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public List<Product> getProducts() {
        if (products != null) {
            return products;
        }
        products = new ArrayList<>();
        return products;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public void setSelectedPeriod(Period selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }

    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // methods for cart info:
    public int getTotalPrice(){
        int total = 0;
        for (ProductItem item : items){
            if (item.getCount() >= 1) {
                total += item.getPriceInt() * item.getCount();
            }
        }
        return total;
    }

    public int getDiscountPrice(){
        int discount = 0;
        for (ProductItem item : items){
            if (item.getCount() >= 1) {
                discount += item.getPriceDiscountInt() * item.getCount();
            }
        }
        return discount;
    }

    public int getPackingPrice(){
        int packingPrice = 0;
        for (ProductItem item : items){
            if (item.getCount() >= 1) {
                packingPrice += item.getPackingPriceInt() * item.getCount();
            }
        }
        return packingPrice;
    }

    public int getShippingCost(){
        if (selectedAddress != null) {
            Area selectedArea = selectedAddress.getLocation().getArea();
            for (StoreArea storeArea : store.getAreas()){
                if (storeArea.getArea().getId() == selectedArea.getId()) {
                    return storeArea.getPriceInt();
                }
            }
        }
        return 0;
    }

    public int getTax(){
        int tax = 0;
        if (store.getTaxPercent() > 0) {
            for (ProductItem item : items){
                if (item.getCount() >= 1) {
                    tax += ((item.getPriceInt() * store.getTaxPercent())/100) * item.getCount();
                }
            }
        }
        return tax;
    }

    public int getSumTotal(){
        return getTotalPrice() + getPackingPrice() + getShippingCost() + getTax() - getDiscountPrice();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeParcelable(selectedPeriod, flags);
        dest.writeParcelable(selectedAddress, flags);
        dest.writeParcelable(store, flags);
        dest.writeTypedList(products);
    }
}
