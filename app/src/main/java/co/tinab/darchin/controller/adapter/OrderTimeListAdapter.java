package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.Period;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 3/6/2018.
 */

public class OrderTimeListAdapter extends
        RecyclerView.Adapter<OrderTimeListAdapter.ViewHolder> {
    private List<Period> periods;
    private int mSelectedItem = -1;

    public void setSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public interface ItemSelectedListener{
        void onItemSelected(Period period);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public OrderTimeListAdapter(List<Period> periods){
        this.periods = periods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_simple_text,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mSelectedItem == position) {
            holder.txt.setBackground(ContextCompat.getDrawable(holder.txt.getContext(),R.drawable.setting_btn_bk));
        }else {
            holder.txt.setBackground(null);
        }
        Period period = periods.get(position);
        holder.txt.setText(holder.view.getContext()
                .getString(R.string.from_to_pattern,period.getFrom(),period.getTo()));
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewLight txt;
        private View view;
        private ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txt = itemView.findViewById(R.id.txt);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, periods.size());
                    listener.onItemSelected(periods.get(mSelectedItem));
                }
            };
            itemView.setOnClickListener(onClickListener);
        }
    }
}
