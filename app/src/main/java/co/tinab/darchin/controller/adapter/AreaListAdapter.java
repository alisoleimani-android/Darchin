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
import co.tinab.darchin.model.address.AreaSearch;

/**
 * Created by A.S.R on 2/19/2018.
 */

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.ViewHolder>{
    private int mSelectedItem = -1;
    private List<AreaSearch> areaList;

    public interface ItemSelectedListener{
        void onItemSelected(AreaSearch areaSearch);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public AreaListAdapter(List<AreaSearch> areaList){
        this.areaList = areaList;
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
        holder.btn.setText(areaList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RadioButton btn;

        private ViewHolder(View itemView) {
            super(itemView);
            btn = (RadioButton) itemView;
            btn.setTypeface(Typeface.createFromAsset(
                    itemView.getContext().getAssets(),"fonts/Light.ttf"));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, areaList.size());
                    listener.onItemSelected(areaList.get(mSelectedItem));
                }
            };

            btn.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }
    }
}