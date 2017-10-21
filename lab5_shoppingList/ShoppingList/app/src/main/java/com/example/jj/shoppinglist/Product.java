package com.example.jj.shoppinglist;

/**
 * Created by jj on 2017/10/21.
 */

public class Product {
    private String name;
    private String price;
    private String type;
    private String information;

    public Product(String name, String price, String type, String information) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.information = information;
    }
    public String getInformation() {
        return information;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }
}
