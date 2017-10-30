package com.example.administrator.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
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

    protected Product(Parcel in) {
        String []data = new String[4];
        in.readStringArray(data);
        this.name =  data[0];
        this.price = data[1];
        this.type = data[2];
        this.information = data[3];
        this.imgResource = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{ name, price, type, information });
        parcel.writeInt(imgResource);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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
