package com.example.administrator.shoppinglist;

/**
 * Created by Administrator on 2017/10/21.
 */

public class Product {
    private String name;
    private String price;
    private String type;
    private String information;
    private int imgResource;

    public Product(String name, String price, String type, String information, int imgResource) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.information = information;
        this.imgResource = imgResource;
    }
    public String getInformation() {
        return information;
    }

    public String getLabel() { return name.substring(0, 1); }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getImgResource() {
        return imgResource;
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

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}
