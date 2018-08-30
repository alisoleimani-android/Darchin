package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.ProductItem;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 2/26/2018.
 */

public class OrderItemsSingleChoiceAdapter extends RecyclerView.Adapter<OrderItemsSingleChoiceAdapter.ViewHolder> {
    private List<ProductItem> itemList;
    private int mSelectedItem = -1;

    public interface ItemSelectedListener{
        void onItemSelected(ProductItem item);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public OrderItemsSingleChoiceAdapter(List<ProductItem> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_choice_medium,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn.setChecked(position == mSelectedItem);
        holder.txt.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewLight txt;
        private RadioButton btn;

        private ViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_name);
            btn = itemView.findViewById(R.id.btn_radio);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,itemList.size());
                    listener.onItemSelected(itemList.get(mSelectedItem));
                }
            };

            btn.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
