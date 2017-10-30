package com.example.administrator.shoppinglist;


public class ShoppingListEvent {
    private int productIndex;
    ShoppingListEvent(int productIndex) {
        this.productIndex = productIndex;
    }

    public int getProductIndex() {
        return productIndex;
    }
}
