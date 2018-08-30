package co.tinab.darchin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class Filter {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("single")
    private boolean single;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("items")
    private List<Item> items;

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public boolean isSingle() {
        return single;
    }

    public static class Item{
        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("filter_id")
        private int filterID;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("slug")
        private String slug;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getFilterID() {
            return filterID;
        }

        public String getSlug() {
            return slug;
        }
    }
}
