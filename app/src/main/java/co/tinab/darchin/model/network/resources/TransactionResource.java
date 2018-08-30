package co.tinab.darchin.model.network.resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.order.Transaction;

/**
 * Created by A.S.R on 4/17/2018.
 */

public class TransactionResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
