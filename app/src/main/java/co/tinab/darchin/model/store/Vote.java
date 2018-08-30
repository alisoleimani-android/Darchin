package co.tinab.darchin.model.store;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 4/11/2018.
 */

public class Vote implements Parcelable {
    @Expose
    @SerializedName("count")
    private int count;

    @Expose
    @SerializedName("average")
    private float average;

    @Expose
    @SerializedName("food_quality")
    private String productQuality;

    @Expose
    @SerializedName("packing_quality")
    private String packingQuality;

    @Expose
    @SerializedName("courier_speed")
    private String courierSpeed;

    @Expose
    @SerializedName("courier_ethics")
    private String courierEthics;

    Vote(){}

    private Vote(Parcel in) {
        count = in.readInt();
        average = in.readFloat();
        productQuality = in.readString();
        packingQuality = in.readString();
        courierSpeed = in.readString();
        courierEthics = in.readString();
    }

    public static final Creator<Vote> CREATOR = new Creator<Vote>() {
        @Override
        public Vote createFromParcel(Parcel in) {
            return new Vote(in);
        }

        @Override
        public Vote[] newArray(int size) {
            return new Vote[size];
        }
    };

    public int getCount() {
        return count;
    }

    public String getAverageString() {
        return String.format(Locale.getDefault(),"%.1f", average);
    }

    public float getAverage() {
        return average;
    }

    public Drawable getProgressBackground(float value, Context context){
        Drawable drawable = null;
        if (value >= 0.0f && value <= 1.6f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.progress_red_bk);

        } else if (value > 1.6f && value <= 3.2f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.progress_yellow_bk);

        } else if (value > 3.2f && value <= 5.0f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.progress_green_bk);

        }
        return drawable;
    }

    public Drawable getScoreBackground(Context context){
        Drawable drawable = null;
        if (average >= 0.0f && average <= 1.6f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.score_red_bk);

        } else if (average > 1.6f && average <= 3.2f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.score_yellow_bk);

        } else if (average > 3.2f && average <= 5.0f) {
            drawable = ContextCompat.getDrawable(context, R.drawable.score_green_bk);

        }
        return drawable;
    }

    public float getCourierEthics() {
        try {
            return Float.parseFloat(courierEthics);
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0f;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0f;
        }
    }

    public String getCourierEthicsStr(){
        return String.format(Locale.getDefault(),"%.1f", getCourierEthics());
    }

    public float getCourierSpeed() {
        try {
            return Float.parseFloat(courierSpeed);
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0f;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0f;
        }
    }

    public String getCourierSpeedStr(){
        return String.format(Locale.getDefault(),"%.1f", getCourierSpeed());
    }

    public float getProductQuality() {
        try {
            return Float.parseFloat(productQuality);
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0f;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0f;
        }
    }

    public String getProductQualityStr(){
        return String.format(Locale.getDefault(),"%.1f", getProductQuality());
    }

    public float getPackingQuality() {
        try {
            return Float.parseFloat(packingQuality);
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0f;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0f;
        }
    }

    public String getPackingQualityStr(){
        return String.format(Locale.getDefault(),"%.1f", getPackingQuality());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeFloat(average);
        dest.writeString(productQuality);
        dest.writeString(packingQuality);
        dest.writeString(courierSpeed);
        dest.writeString(courierEthics);
    }
}
