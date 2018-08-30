package co.tinab.darchin.controller.interfaces;

import co.tinab.darchin.model.store.ProductItem;

/**
 * Created by A.S.R on 3/10/2018.
 */

public interface ProductItemClickListener {
    void onAddItem(ProductItem item,int position);
    void onRemoveItem(ProductItem item,int position);
}
