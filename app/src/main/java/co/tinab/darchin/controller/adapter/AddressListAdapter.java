package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 2/19/2018.
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private List<Address> addressList;

    public AddressListAdapter(List<Address> addressList){
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_address_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(addressList.get(position));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextViewNormal txtTag;
        private TextViewLight txtAddress;
        private View row;

        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            txtTag = itemView.findViewById(R.id.txt_tag);
            txtAddress = itemView.findViewById(R.id.txt_address);
        }
        private void bind(Address address){
            txtTag.setText(address.getTag());
            txtAddress.setText(address.getFullAddress(row.getContext()));
        }
    }
}
