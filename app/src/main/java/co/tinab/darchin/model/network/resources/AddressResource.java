package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.model.Constant;

/**
 * Created by A.S.R on 3/24/2018.
 */

public class AddressResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Address> addressList;

    @Nullable
    public List<Address> getAddressList() {
        return addressList;
    }

}
