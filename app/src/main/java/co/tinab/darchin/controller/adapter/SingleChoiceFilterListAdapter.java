package co.tinab.darchin.controller.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.Filter;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class SingleChoiceFilterListAdapter extends RecyclerView.Adapter<SingleChoiceFilterListAdapter.ViewHolder> {
    private int mSelectedItem = -1;

    private List<Filter.Item> itemList;
    public interface ItemSelectedListener{
        void onItemSelected(Filter.Item item);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public SingleChoiceFilterListAdapter(List<Filter.Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_choice_small,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.radioButton.setChecked(position == mSelectedItem);
        holder.radioButton.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;

        private ViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView;
            radioButton.setTypeface(Typeface.createFromAsset(
                    itemView.getContext().getAssets(),"fonts/Light.ttf"));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,itemList.size());
                    listener.onItemSelected(itemList.get(mSelectedItem));
                }
            };

            radioButton.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
