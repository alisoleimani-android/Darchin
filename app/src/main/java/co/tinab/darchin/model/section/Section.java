package co.tinab.darchin.model.section;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by A.S.R on 1/1/2018.
 */

public class Section implements Parcelable {
    @Expose
    @SerializedName("id")
    private int sectionId;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("content")
    private List<?> contents;

    private String position;
    private String page;

    protected Section(Parcel in) {
        sectionId = in.readInt();
        type = in.readString();
        name = in.readString();
        position = in.readString();
        page = in.readString();
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public int getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public JsonArray getContents() {
        Gson gson = new Gson();
        return gson.toJsonTree(contents).getAsJsonArray();
    }

    public String getPosition() {
        return position;
    }

    public String getPage() {
        return page;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sectionId);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(position);
        dest.writeString(page);
    }
}
