package com.example.administrator.lab10_db;

/**
 * Created by Administrator on 2017/12/5.
 */

public class Person{
    private String name;
    private String birthday;
    private String gift;
    Person(String name, String birthday, String gift) {
        this.name = name;
        this.birthday = birthday;
        this.gift = gift;
    }

    public String getGift() {
        return gift;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String birthday) {
        this.birthday = birthday;
    }
}