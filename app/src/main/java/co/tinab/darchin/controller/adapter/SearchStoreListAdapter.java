package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import co.tinab.darchin.R;
import co.tinab.darchin.model.store.Store;
import co.tinab.darchin.view.toolbox.TextViewLight;

/**
 * Created by A.S.R on 4/10/2018.
 */

public class SearchStoreListAdapter extends RecyclerView.Adapter<SearchStoreListAdapter.ViewHolder> {
    private List<Store> stores;

    public SearchStoreListAdapter(List<Store> stores){
        this.stores = stores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(stores.get(position));
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgName;
        private TextViewLight txtName;

        private ViewHolder(View itemView) {
            super(itemView);
            imgName = itemView.findViewById(R.id.ic_name);
            txtName = itemView.findViewById(R.id.txt_name);
        }

        private void bind(Store store){
            imgName.setImageResource(R.drawable.ic_store_24dp);
            txtName.setText(store.getName());
        }
    }
}
