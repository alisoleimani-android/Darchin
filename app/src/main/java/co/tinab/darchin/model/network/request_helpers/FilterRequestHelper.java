package co.tinab.darchin.model.network.request_helpers;

import co.tinab.darchin.model.network.interfaces.FilterInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.FilterResource;
import retrofit2.Call;

/**
 * Created by A.S.R on 3/31/2018.
 */

public class FilterRequestHelper {
    public static FilterRequestHelper getInstance(){
        return new FilterRequestHelper();
    }

    public Call<FilterResource> getFilterList(){
        FilterInterface filterInterface = MyRetrofit.getRetrofitInstance().create(FilterInterface.class);
        return filterInterface.getFilterList();
    }
}
