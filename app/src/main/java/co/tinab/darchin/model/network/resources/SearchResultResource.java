package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.address.AreaSearch;
import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.Store;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class SearchResultResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response{
        @Expose
        @SerializedName("addresses")
        private List<AreaSearch> areas;

        @Expose
        @SerializedName("stores")
        private List<Store> stores;

        @Expose
        @SerializedName("products")
        private List<Product> products;

        public List<AreaSearch> getAreas() {
            return areas;
        }

        public List<Product> getProducts() {
            return products;
        }

        public List<Store> getStores() {
            return stores;
        }
    }
}
