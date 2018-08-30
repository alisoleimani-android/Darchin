package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 3/20/2018.
 */

public class BasicResource {
    @Expose
    @SerializedName(Constant.STATUS)
    private String status;

    @Expose
    @SerializedName(Constant.MESSAGE)
    private String message;

    @Expose
    @SerializedName("meta")
    private Meta meta;

    @Expose
    @SerializedName("links")
    private Link link;

    @Nullable
    public Link getLink() {
        return link;
    }

    @Nullable
    public Meta getMeta() {
        return meta;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Nullable
    public String getStatus() {
        return status;
    }


    public class Link{
        @Expose
        @SerializedName("first")
        private String first;

        @Expose
        @SerializedName("last")
        private String last;

        @Expose
        @SerializedName("prev")
        private String prev;

        @Expose
        @SerializedName("next")
        private String next;

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public String getNext() {
            return next;
        }

        public String getPrev() {
            return prev;
        }
    }

    public class Meta{
        @Expose
        @SerializedName("current_page")
        private int currentPage;

        @Expose
        @SerializedName("from")
        private int from;

        @Expose
        @SerializedName("last_page")
        private int last;

        @Expose
        @SerializedName("path")
        private String path;

        @Expose
        @SerializedName("per_page")
        private int perPage;

        @Expose
        @SerializedName("to")
        private int to;

        @Expose
        @SerializedName("total")
        private int total;


        public int getCurrentPage() {
            return currentPage;
        }

        public int getFrom() {
            return from;
        }

        public int getLast() {
            return last;
        }

        public int getPerPage() {
            return perPage;
        }

        public int getTo() {
            return to;
        }

        public int getTotal() {
            return total;
        }

        public String getPath() {
            return path;
        }
    }
}
