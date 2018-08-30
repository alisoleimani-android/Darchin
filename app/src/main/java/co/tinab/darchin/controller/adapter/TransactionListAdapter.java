package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.order.Transaction;
import co.tinab.darchin.view.toolbox.MoneyTextView;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 3/7/2018.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {
    private List<Transaction> transactionList;

    public TransactionListAdapter(List<Transaction> transactionList){
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_transaction_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(transactionList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewNormal txtType;
        private MoneyTextView txtPrice;
        private TextViewLight txtDate,txtDescription;
        private View view;

        private ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtType = itemView.findViewById(R.id.txt_type);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDescription = itemView.findViewById(R.id.txt_desc);
        }

        private void bind(Transaction transaction){
            txtType.setText(transaction.getType(view.getContext()));

            txtPrice.setText(transaction.getPrice());
            txtPrice.setTextColor(transaction.getPriceColorRes(view.getContext()));

            txtDate.setText(transaction.getDate());
            txtDescription.setText(transaction.getDescription());
        }
    }
}
