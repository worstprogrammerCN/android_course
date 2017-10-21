package com.example.jj.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.example.jj.shoppinglist.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setAdapter(new MyAdapter(getData()));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private ArrayList<Product> getData() {
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product("Enchated Forest","¥ 5.00",   "作者",   "Johanna Basford"));
        list.add(new Product("Arla Milk","¥ 59.00",   "产地",  "德国"));
        list.add(new Product("Devondale Milk","¥ 79.00" ,   "产地",   "澳大利亚"));
        list.add(new Product("Kindle Oasis","¥ 2399.00",  "版本", "8GB"));
        list.add(new Product("waitrose 早餐麦片","¥ 179.00", "重量", "2Kg"));
        list.add(new Product("Mcvitie's 饼干","¥ 14.90", "产地",   "英国"));
        list.add(new Product("Ferrero Rocher","¥ 132.59", "重量",  "300g"));
        list.add(new Product("Maltesers", "¥ 141.43", "重量",  "118g"));
        list.add(new Product("Lindt","¥ 139.43", "重量",   "249g"));
        list.add(new Product("Borggreve", "¥ 28.90",   "重量",  "640g"));
        return list;
    }
}
