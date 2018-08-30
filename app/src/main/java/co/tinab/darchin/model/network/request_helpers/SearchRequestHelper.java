package co.tinab.darchin.model.network.request_helpers;

import co.tinab.darchin.model.network.interfaces.SearchInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.SearchResultResource;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class SearchRequestHelper {
    public static SearchRequestHelper getInstance(){
        return new SearchRequestHelper();
    }

    public Call<SearchResultResource> search(int cityId,String string){
        SearchInterface searchInterface = MyRetrofit.getRetrofitInstance().create(SearchInterface.class);
        return searchInterface.search(String.valueOf(cityId),string);
    }
}
