package co.tinab.darchin.model.store;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.model.address.Location;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.R;

/**
 * Created by A.S.R on 1/2/2018.
 */

public class Store implements Parcelable{
    @Expose
    @SerializedName("id")
    private int storeId;

    @Expose
    @SerializedName("picture")
    private String picture;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("location")
    private Location location;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("store_types")
    private List<StoreType> storeTypes = new ArrayList<>();

    @Expose
    @SerializedName("areas")
    private List<StoreArea> areas = new ArrayList<>();

    @Expose
    @SerializedName("hours")
    private List<Hour> hours = new ArrayList<>();

    @Expose
    @SerializedName("latitude")
    private double latitude;

    @Expose
    @SerializedName("longitude")
    private double longitude;

    @Expose
    @SerializedName("minimum_order")
    private int minimumOrder;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("order_active")
    private boolean orderActive;

    @Expose
    @SerializedName("stop_ordering")
    private boolean stopOrdering;

    @Expose
    @SerializedName("tax_percent")
    private int taxPercent;

    @Expose
    @SerializedName("categories")
    private List<Category> categories = new ArrayList<>();

    @Expose
    @SerializedName("product_types")
    private List<ProductType> productTypes = new ArrayList<>();

    @Expose
    @SerializedName("votes")
    private Vote vote;

    @Expose
    @SerializedName("discount")
    private String discount;

    @Expose
    @SerializedName("slug")
    private String slug;

    private Cart cart;

    public Store(){}

    private Store(Parcel in) {
        picture = in.readString();
        name = in.readString();
        address = in.readString();
        storeId = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
        vote = in.readParcelable(Vote.class.getClassLoader());
        in.readTypedList(areas, StoreArea.CREATOR);
        in.readTypedList(storeTypes,StoreType.CREATOR);
        in.readTypedList(hours,Hour.CREATOR);
        in.readTypedList(categories, Category.CREATOR);
        in.readTypedList(productTypes, ProductType.CREATOR);
        latitude = in.readDouble();
        longitude = in.readDouble();
        minimumOrder = in.readInt();
        status = in.readString();
        slug = in.readString();
        orderActive = in.readByte() != 0;
        stopOrdering = in.readByte() != 0;
        taxPercent = in.readInt();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public Cart getCart() {
        return cart;
    }

    public void setCart() {
        this.cart = Cart.newInstance(this);
    }

    public String getDiscount() {
        return String.valueOf(getDiscountInteger()).concat("%");
    }

    private int getDiscountInteger(){
        return (int) Float.parseFloat(discount);
    }

    public boolean hasDiscount(){
        return discount != null && !discount.isEmpty() && getDiscountInteger() > 0;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getTaxPercent() {
        return taxPercent;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public Vote getVote() {
        if (vote == null) {
            return new Vote();
        }
        return vote;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public List<StoreArea> getAreas() {
        if (areas == null) {
            return new ArrayList<>();
        }
        return areas;
    }

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public List<Category> getCategories() {
        if (categories == null) {
            return new ArrayList<>();
        }
        return categories;
    }

    public String getCover(){
        List<String> images = new ArrayList<>();

        for (Category category : categories){
            for (Product product : category.getProducts()){
                for (ProductItem item : product.getItems()){
                    if (item.getImage() != null) {
                        images.add(Constant.PRODUCT_THUMBNAIL_PATH.concat(item.getImage()));
                    }
                }
            }
        }
        if (!images.isEmpty()) {
            if (images.size() == 1) {
                return images.get(0);
            }
            Random random = new Random();
            return images.get(random.nextInt(images.size()-1));
        }else {
            return Constant.PRODUCT_THUMBNAIL_PATH;
        }
    }

    private List<Hour> getHoursOfDay(String day){
        if (hours == null) {
            return new ArrayList<>();
        }

        List<Hour> hourList = new ArrayList<>();

        for (Hour hour : hours){
            if (hour.getDay().equals(day)) {
                hourList.add(hour);
            }
        }

        // sort list
        int n = hourList.size();
        Hour temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if(hourList.get(j-1).getOpen().after(hourList.get(j).getOpen())){
                    //swap elements
                    temp = hourList.get(j-1);
                    hourList.set(j-1,hourList.get(j));
                    hourList.set(j,temp);
                }

            }
        }
        return hourList;
    }

    public String getHoursOfDayStr(Context context, String day){
        String strHour = "";
        List<Hour> hours = getHoursOfDay(day);
        if (!hours.isEmpty()) {
            for (int i = 0 ; i < hours.size() ; i++) {
                if (i < hours.size() - 1){
                    strHour = strHour.concat(context.getString(R.string.from_to_pattern,
                            hours.get(i).getOpenStr(),hours.get(i).getCloseStr()).concat("\t,\t"));
                }else if (i == hours.size() - 1){
                    strHour = strHour.concat(context.getString(R.string.from_to_pattern,
                            hours.get(i).getOpenStr(),hours.get(i).getCloseStr()));
                }
            }
        }else {
            strHour = context.getString(R.string.not_added);
        }
        return strHour;
    }

    public boolean isOrderAccepted(Date time){
        if (time != null) {
            List<Hour> hours = getHoursOfDay(MyDateTime.getToday());
            for (Hour hour : hours) {
                if ((time.after(hour.getOpen()) && time.before(hour.getClose())) && (hour.isDelivery() || hour.isInside())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStoreAvailable(){
        return !stopOrdering && orderActive;
    }

    public Map<String,String> getDeliveryCost(){
        if (areas == null) {
            return new HashMap<>();
        }

        Map<String,String> map = new HashMap<>();

        long max = 0;
        for (StoreArea area : areas){
            long price = Long.valueOf(areas.get(areas.indexOf(area)).getPriceStr());
            if (price > max){
                max = price;
            }
        }
        map.put("max",String.valueOf(max));

        long min = max;
        for (StoreArea area : areas){
            long price = Long.valueOf(areas.get(areas.indexOf(area)).getPriceStr());
            if (price < min){
                min = price;
            }
        }
        if (min == max) {
            min = 0;
        }
        map.put("min",String.valueOf(min));

        return map;
    }

    public String getPicture() {
        if (picture != null) {
            return Constant.LOGO_PATH.concat(picture);
        }else {
            return Constant.LOGO_PATH;
        }
    }

    public String getAddress() {
        if (address == null) {
            return "";
        }
        return address;
    }

    public String getFullAddress(Context context){
        if (getLocation() != null
                && getLocation().getArea() != null
                && getLocation().getArea().getCity() != null
                && getLocation().getArea().getCity().getName() != null
                && getAddress() != null) {

            return getLocation().getArea().getCity().getName().concat(" , ")
                    .concat(getLocation().getArea().getName().concat(" , ").concat(getAddress()));
        }
        return context.getString(R.string.unknown);
    }

    public Location getLocation() {
        return location;
    }

    public List<StoreType> getStoreTypes() {
        if (storeTypes == null) {
            return new ArrayList<>();
        }
        return storeTypes;
    }

    public List<Period> getTimePeriods(String type, boolean isInside, boolean isDelivery){
        List<Period> totalPeriods = new ArrayList<>();
        List<Hour> hours = new ArrayList<>();

        if (type.equals("tomorrow")) {
            hours.addAll(getHoursOfDay(MyDateTime.getTomorrow()));
        }
        if (type.equals("today")) {
            hours.addAll(getHoursOfDay(MyDateTime.getToday()));
        }
        if (isDelivery) {
            for (Hour hour : hours){
                if (hour.isDelivery() && hour.isOrderActive()) {
                    Date open = hour.getOpen();
                    Date close = hour.getClose();
                    while (open.before(close)) {
                        // new time before adding 30 min
                        Date from = open;

                        // adding 30 min
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(open);
                        cal.add(Calendar.MINUTE, 30);
                        open = cal.getTime();

                        Date to = open;

                        totalPeriods.add(new Period(from,to));
                    }
                }
            }
        }

        if (isInside) {
            for (Hour hour : hours){
                if (hour.isInside() && hour.isOrderActive()) {
                    Date open = hour.getOpen();
                    Date close = hour.getClose();
                    while (open.before(close)) {
                        // new time before adding 30 min
                        Date from = open;

                        // adding 30 min
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(open);
                        cal.add(Calendar.MINUTE, 30);
                        open = cal.getTime();

                        Date to = open;

                        totalPeriods.add(new Period(from,to));
                    }
                }
            }
        }

        if (type.equals("today")) {
            Date currentTime = MyDateTime.getCurrentTime();
            List<Period> periods = new ArrayList<>();
            for (Period period : totalPeriods){
                if (currentTime != null && currentTime.before(period.getToTime())) {
                    period.setType(type);
                    periods.add(period);
                }
            }
            if (periods.size() > 1){
                periods.remove(0);
            }
            return periods;
        }

        if (type.equals("tomorrow")) {
            for (Period period : totalPeriods){
                period.setType(type);
            }
        }

        return totalPeriods;
    }

    public String getServiceRange(Context context){
        if (areas == null) {
            return "";
        }

        String serviceRange = "";
        if (areas.size() > 1) {
            if (hasTooManyServiceRange()) {
                for (int i = 0 ; i < 10 ; i++) {
                    if (i == 9) {
                        serviceRange = serviceRange.concat(areas.get(i).getName().concat(" ,..."));
                    }else {
                        serviceRange = serviceRange.concat(areas.get(i).getName().concat(" , "));
                    }
                }
            }else {
                for (int i = 0 ; i < areas.size() ; i++) {
                    if (i == areas.size()-1) {
                        serviceRange = serviceRange.concat(areas.get(i).getName());
                    }else {
                        serviceRange = serviceRange.concat(areas.get(i).getName().concat(" , "));
                    }
                }
            }
        }else if (areas.size() == 1){
            serviceRange = areas.get(0).getName();
        }else {
            serviceRange = context.getString(R.string.not_added);
        }
        return serviceRange;
    }

    public String getRestOfServiceRange(){
        if (areas == null) {
            return "";
        }

        String serviceRange = "";
        for (int i = 10 ; i < areas.size() ; i++) {
            if (i == areas.size()-1) {
                serviceRange = serviceRange.concat(areas.get(i).getName());
            }else {
                serviceRange = serviceRange.concat(areas.get(i).getName().concat(" , "));
            }
        }
        return serviceRange;
    }

    public boolean hasTooManyServiceRange(){
        return areas != null && areas.size() > 10;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picture);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeTypedList(storeTypes);
        dest.writeTypedList(productTypes);
        dest.writeInt(storeId);
        dest.writeParcelable(location,flags);
        dest.writeParcelable(vote,flags);
        dest.writeTypedList(areas);
        dest.writeTypedList(hours);
        dest.writeTypedList(categories);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(minimumOrder);
        dest.writeString(slug);
        dest.writeString(status);
        dest.writeByte((byte) (orderActive ? 1 : 0));
        dest.writeByte((byte) (stopOrdering ? 1 : 0));
        dest.writeInt(taxPercent);
    }
}
