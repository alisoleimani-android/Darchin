package co.tinab.darchin.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.tinab.darchin.model.address.City;
import co.tinab.darchin.model.database.DbHelper;
import co.tinab.darchin.model.section.Section;
import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 3/13/2018.
 */

public class User implements Parcelable {
    private static User user;

    @Expose
    @SerializedName("id")
    private int userID;

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("credit")
    private String credit;

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("referral_link")
    private String referralLink;

    @Expose
    @SerializedName("api_token")
    private String apiToken;

    @Expose
    @SerializedName("order_active")
    private boolean orderActive;

    private List<Store> favoriteList = new ArrayList<>();

    public User(){}

    public static User getInstance(Context context){
        if (User.user == null) {
            Gson gson = new Gson();
            String json = DbHelper.getInstance().select(context,"user");
            User user = gson.fromJson(json, User.class);
            if (user != null) {
                User.user = user;
            }else{
                User.user = new User();
            }
        }
        return User.user;
    }

    private void deleteInstance(Context context){
        User.user = null;
        DbHelper.getInstance().delete(context,"user");
    }

    public static void saveInstance(Context context,User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        DbHelper.getInstance().insert(context,"user",json);
        User.user = user;
    }

    protected User(Parcel in) {
        userID = in.readInt();
        username = in.readString();
        name = in.readString();
        status = in.readString();
        credit = in.readString();
        phone = in.readString();
        referralLink = in.readString();
        apiToken = in.readString();
        orderActive = in.readByte() != 0;
        favoriteList = in.createTypedArrayList(Store.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getter:
    public int getUserID() {
        return userID;
    }

    public String getPhone() {
        if (phone == null) {
            return "";
        }
        return phone;
    }

    public List<Store> getFavoriteList() {
        if (favoriteList == null) {
            return new ArrayList<>();
        }
        return favoriteList;
    }

    public boolean isFavorite(int storeId){
        if (favoriteList != null && !favoriteList.isEmpty()) {
            for (Store store : favoriteList){
                if (store != null) {
                    if (store.getStoreId() == storeId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void removeFavorite(int storeId){
        if (favoriteList != null && !favoriteList.isEmpty()) {
            for (Iterator<Store> iterator = favoriteList.iterator(); iterator.hasNext(); ) {
                Store store = iterator.next();
                if (store != null && store.getStoreId() == storeId) {
                    iterator.remove();
                }
            }
        }
    }

    public String getReferralLink() {
        if (referralLink == null) {
            return "";
        }
        return referralLink;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return apiToken;
    }

    public String getCredit() {
        if (credit == null) {
            return "0";
        }
        return credit;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getStatus() {
        if (status == null) {
            return "deactive";
        }
        return status;
    }

    @Nullable
    public City getCity(Context context) {
        Gson gson = new Gson();
        String json = DbHelper.getInstance().select(context,"city");
        City city = gson.fromJson(json, City.class);
        if (city != null) {
            return city;
        }
        return null;
    }

    public void setCity(Context context, City city){
        Gson gson = new Gson();
        String json = gson.toJson(city);
        DbHelper.getInstance().insert(context,"city",json);
    }

    public List<Section> getSections(Context context, String page, String position) {
        Gson gson = new Gson();
        String json = DbHelper.getInstance().select(context,"section".concat(page).concat(position));
        Type type = new TypeToken<List<Section>>(){}.getType();
        List<Section> sections = gson.fromJson(json, type);
        if (sections != null) {
            return sections;
        }
        return new ArrayList<>();
    }

    public void setSections(Context context, List<Section> sections, String page, String position){
        Gson gson = new Gson();
        String json = gson.toJson(sections);
        DbHelper.getInstance().insert(context,"section".concat(page).concat(position),json);
    }

    public boolean isOrderActive() {
        return orderActive;
    }

    public void activateUser() {
        this.status = "active";
    }

    public void setFavoriteList(List<Store> favoriteList) {
        this.favoriteList.clear();
        this.favoriteList.addAll(favoriteList);
    }

    public void setOrderActive() {
        this.orderActive = true;
    }

    // Settings:
    public static void setToken(@Nullable Context context, String apiToken) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(Constant.Pref_Token,apiToken).apply();
        }
    }

    private void deleteToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(Constant.Pref_Token).apply();
    }

    public static String getToken(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(Constant.Pref_Token,"");
            return "Bearer ".concat(token);
        }else {
            return null;
        }
    }

    public boolean hasLoggedIn(@NonNull Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.Pref_Has_Login,false);
    }

    public boolean hasSelectedLanguage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.Pref_Has_Selected_Language,false);
    }

    public boolean hasAppIntro(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.Pref_First_Use,false);
    }

    public void setAppIntro(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constant.Pref_First_Use, true).apply();
    }

    public void setSelectedLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constant.Pref_Has_Selected_Language, true).apply();
    }

    public void setLoggedIn(Context context, boolean login) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.Pref_Name,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constant.Pref_Has_Login, login).apply();
    }

    public void delete(Context context){
        user.setLoggedIn(context,false);
        user.deleteToken(context);
        user.deleteInstance(context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(credit);
        dest.writeString(phone);
        dest.writeString(referralLink);
        dest.writeString(apiToken);
        dest.writeByte((byte) (orderActive ? 1 : 0));
        dest.writeTypedList(favoriteList);
    }
}
