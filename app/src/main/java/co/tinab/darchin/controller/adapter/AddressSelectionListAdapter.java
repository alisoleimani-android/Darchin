package co.tinab.darchin.controller.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.util.List;

import co.tinab.darchin.model.address.Address;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.TextViewLight;
import co.tinab.darchin.view.toolbox.TextViewNormal;

/**
 * Created by A.S.R on 3/4/2018.
 */

public class AddressSelectionListAdapter extends RecyclerView.Adapter<AddressSelectionListAdapter.ViewHolder> {
    private List<Address> addressList;
    private int mSelectedItem = -1;

    public void setSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    public interface AddressListListener {
        void onItemSelected(Address address);
        void onBtnEditClicked(Address address);
        void onBtnDeleteClicked(Address address);
    }
    private AddressListListener listener;

    public void addOnAddressListListener(AddressListListener listener){
        this.listener = listener;
    }

    public AddressSelectionListAdapter(List<Address> addressList){
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_address_selection_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnRadio.setChecked(position == mSelectedItem);

        Address address = addressList.get(position);
        holder.txtAddress.setText(address.getFullAddress(holder.row.getContext()));
        holder.txtName.setText(address.getTag());

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RadioButton btnRadio;
        private TextViewLight txtAddress;
        private TextViewNormal txtName;
        private ImageButton btnEdit,btnDelete;
        private View row;

        private ViewHolder(View itemView) {
            super(itemView);
            row = itemView;

            btnRadio = itemView.findViewById(R.id.btn_radio);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtName = itemView.findViewById(R.id.txt_name);

            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnEdit.setOnClickListener(this);

            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(this);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,addressList.size());
                    listener.onItemSelected(addressList.get(mSelectedItem));
                }
            };

            btnRadio.setOnClickListener(onClickListener);
            itemView.setOnClickListener(onClickListener);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_delete:
                    listener.onBtnDeleteClicked(addressList.get(getAdapterPosition()));
                    break;
                case R.id.btn_edit:
                    listener.onBtnEditClicked(addressList.get(getAdapterPosition()));
                    break;
            }
        }
    }
}
