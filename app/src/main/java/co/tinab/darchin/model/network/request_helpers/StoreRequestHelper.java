package co.tinab.darchin.model.network.request_helpers;

import co.tinab.darchin.model.network.interfaces.StoreInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.CommentCollectionResource;
import co.tinab.darchin.model.network.resources.StoreResource;
import co.tinab.darchin.model.network.resources.StoreCollectionResource;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/9/2018.
 */

public class StoreRequestHelper {
    public static StoreRequestHelper getInstance(){
        return new StoreRequestHelper();
    }

    public Call<StoreResource> getStore(int storeId){
        StoreInterface storeInterface = MyRetrofit.getRetrofitInstance().create(StoreInterface.class);
        return storeInterface.getStore(String.valueOf(storeId));
    }

    public Call<StoreCollectionResource> getStoreServiceByLocation(int locationId){
        StoreInterface storeInterface = MyRetrofit.getRetrofitInstance().create(StoreInterface.class);
        return storeInterface.getStoreServiceByLocation(String.valueOf(locationId));
    }

    public Call<CommentCollectionResource> getComments(String url){
        StoreInterface storeInterface = MyRetrofit.getRetrofitInstance().create(StoreInterface.class);
        return storeInterface.getComments(url);
    }
}
