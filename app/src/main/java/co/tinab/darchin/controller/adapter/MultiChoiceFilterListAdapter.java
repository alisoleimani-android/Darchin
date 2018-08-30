package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.Filter;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 1/14/2018.
 */

public class MultiChoiceFilterListAdapter extends RecyclerView.Adapter<MultiChoiceFilterListAdapter.ViewHolder> {
    private List<Filter.Item> itemList;
    public interface ItemSelectedListener{
        void onItemSelected(boolean isChecked, Filter.Item item);
    }
    private ItemSelectedListener listener;

    public void setOnItemSelectedListener(ItemSelectedListener listener){
        this.listener = listener;
    }

    public MultiChoiceFilterListAdapter(List<Filter.Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multi_choice,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextViewLight txtName;

        private ViewHolder(final View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            txtName = itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onItemSelected(isChecked,itemList.get(getAdapterPosition()));
                }
            });
        }

        private void bind(Filter.Item item){
            txtName.setText(item.getName());
        }
    }
}
