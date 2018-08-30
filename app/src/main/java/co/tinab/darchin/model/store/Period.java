package co.tinab.darchin.model.store;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.tinab.darchin.controller.tools.MyDateTime;
import co.tinab.darchin.R;

/**
 * Created by A.S.R on 4/8/2018.
 */

public class Period implements Parcelable{
    private String from,to,type;
    private Date toTime;

    Period(Date fromTime,Date toTime){
        this.toTime = toTime;

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm", Locale.US);
        this.from = parser.format(fromTime);
        this.to = parser.format(toTime);
    }

    private Period(Parcel in) {
        from = in.readString();
        to = in.readString();
    }

    public static final Creator<Period> CREATOR = new Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };

    Date getToTime(){
        return toTime;
    }

    public String getTo() {
        if (to == null) {
            return "";
        }
        return to;
    }

    public String getFrom() {
        if (from == null) {
            return "";
        }
        return from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderDateTime(){
        if (type.equals("today")) {
            return MyDateTime.getTodayDate().concat(" ").concat(to).concat(":00");
        }
        if (type.equals("tomorrow")) {
            return MyDateTime.getTomorrowDate().concat(" ").concat(to).concat(":00");
        }
        return "";
    }

    public String getOrderTimeText(Context context){
        if (type.equals("today")) {
            return context.getString(R.string.today).concat(" : ")
                    .concat(context.getString(R.string.from_to_pattern,from,to));
        }
        if (type.equals("tomorrow")) {
            return context.getString(R.string.tomorrow).concat(" : ")
                    .concat(context.getString(R.string.from_to_pattern,from,to));
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
    }
}
