package co.tinab.darchin.model.section;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by A.S.R on 3/29/2018.
 */

public class Divider implements Parcelable{
    private String type,name,desc;
    private int height;

    public Divider(String type,String name, String desc, int height){
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.height = height;
    }

    private Divider(Parcel in) {
        type = in.readString();
        name = in.readString();
        desc = in.readString();
        height = in.readInt();
    }

    public static final Creator<Divider> CREATOR = new Creator<Divider>() {
        @Override
        public Divider createFromParcel(Parcel in) {
            return new Divider(in);
        }

        @Override
        public Divider[] newArray(int size) {
            return new Divider[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeInt(height);
    }
}
