package co.tinab.darchin.controller.interfaces;

import co.tinab.darchin.model.store.Product;
import co.tinab.darchin.model.store.ProductItem;

/**
 * Created by A.S.R on 4/13/2018.
 */

public interface ProductClickListener {
    void onAddItem(Product product, ProductItem item, int productPosition);
    void onRemoveItem(Product product, ProductItem item, int productPosition);
}
