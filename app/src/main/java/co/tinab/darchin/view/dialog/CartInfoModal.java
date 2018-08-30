package co.tinab.darchin.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tinab.darchin.model.store.Cart;
import co.tinab.darchin.R;
import co.tinab.darchin.view.toolbox.MoneyTextView;

/**
 * Created by A.S.R on 3/3/2018.
 */

public class CartInfoModal extends BottomSheetDialogFragment {
    private Cart cart;

    public static CartInfoModal newInstance(Cart cart){
        CartInfoModal modal = new CartInfoModal();
        Bundle args = new Bundle();
        args.putParcelable("cart",cart);
        modal.setArguments(args);
        return modal;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable("cart");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_cart_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MoneyTextView txtTotal,txtDiscount,txtPackingPrice,txtShippingCost,txtTax,txtSumTotal;

        txtTotal = view.findViewById(R.id.txt_total);
        txtTotal.setText(String.valueOf(cart.getTotalPrice()));

        txtDiscount = view.findViewById(R.id.txt_discount);
        txtDiscount.setText(String.valueOf(cart.getDiscountPrice()));

        txtPackingPrice = view.findViewById(R.id.txt_packing);
        txtPackingPrice.setText(String.valueOf(cart.getPackingPrice()));

        txtShippingCost = view.findViewById(R.id.txt_delivery);
        txtShippingCost.setText(String.valueOf(cart.getShippingCost()));

        txtTax = view.findViewById(R.id.txt_tax);
        txtTax.setText(String.valueOf(cart.getTax()));

        txtSumTotal = view.findViewById(R.id.txt_sum_total);
        txtSumTotal.setText(String.valueOf(cart.getSumTotal()));

    }
}
