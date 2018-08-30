package co.tinab.darchin.model.network.request_helpers;

import java.util.LinkedHashMap;
import java.util.Map;

import co.tinab.darchin.model.network.interfaces.SectionInterface;
import co.tinab.darchin.model.network.MyRetrofit;
import co.tinab.darchin.model.network.resources.SectionResource;
import retrofit2.Call;

/**
 * Created by A.S.R on 4/8/2018.
 */

public class SectionRequestHelper {
    public static SectionRequestHelper getInstance(){
        return new SectionRequestHelper();
    }

    public Call<SectionResource> getSections(String page, String position, String cityId){
        Map<String,String> params = new LinkedHashMap<>();
        params.put("page",page);
        params.put("position",position);
        params.put("city_id",cityId);
        params.put("device","mobile");

        SectionInterface sectionInterface = MyRetrofit.getRetrofitInstance().create(SectionInterface.class);
        return sectionInterface.getSections(params);
    }
}
