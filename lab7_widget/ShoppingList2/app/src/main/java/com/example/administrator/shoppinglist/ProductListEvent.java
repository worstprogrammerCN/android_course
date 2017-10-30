package com.example.administrator.shoppinglist;

/**
 * Created by Administrator on 2017/10/28.
 */

public class ProductListEvent {
    private int productIndex;
    ProductListEvent(int productIndex) {
        this.productIndex = productIndex;
    }

    public int getProductIndex() {
        return productIndex;
    }
}
