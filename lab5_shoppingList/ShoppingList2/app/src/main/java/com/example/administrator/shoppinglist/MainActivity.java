package com.example.administrator.shoppinglist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.administrator.shoppinglist.MyAdapter;


public class MainActivity extends AppCompatActivity {
    LinearLayout shopListLayout;
    ListView productListView;
    SimpleAdapter productListAdapter;
    RecyclerView rv;
    ArrayList<Map<String, Object>> productList = new ArrayList<>();
    FloatingActionButton fab;
    boolean isShopList = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShopList) {
                    rv.setVisibility(View.VISIBLE);
                    shopListLayout.setVisibility(View.INVISIBLE);
                } else {
                    rv.setVisibility(View.INVISIBLE);
                    shopListLayout.setVisibility(View.VISIBLE);
                }
                isShopList = !isShopList;
            }
        });
        ArrayList<Map<String, Object>> list = new ArrayList<>();

        shopListLayout = (LinearLayout)findViewById(R.id.shopListLayout);

        productListView = (ListView)findViewById(R.id.productListView);
        productListAdapter = new SimpleAdapter(this,
                productList,
                R.layout.shop_list_item,
                new String[]{"label", "name", "price"},
                new int[] { R.id.shopListlabel, R.id.shopListname, R.id.shopListprice } );
        productListView.setAdapter(productListAdapter);
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int productIndex, long l) {
                final int index =  productIndex;
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("移除商品")
                        .setMessage("从购物车移除" + productList.get(productIndex).get("name") + "?")
                        .setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                productList.remove(index);
                                productListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        })
                        .show();
                return true;
            }
        });
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, product_detail.class);
                Bundle data = new Bundle();
                HashMap<String, Object> m =  (HashMap<String, Object>) productList.get(i);
                data.putString("name", (String)m.get("name"));
                data.putString("price", (String)m.get("price"));
                data.putString("type", (String)m.get("type"));
                data.putString("information", (String)m.get("information"));
                data.putInt("imgResource", (int)m.get("imgResource"));
                data.putInt("productIndex", i);

                intent.putExtras(data);
                startActivityForResult(intent, 1);
            }
        });

        rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter(getData()));

    }

    private ArrayList<Product> getData() {
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product("Enchated Forest", "¥ 5.00",   "作者",   "Johanna Basford", R.drawable.enchated_forest));
        list.add(new Product("Arla Milk", "¥ 59.00",   "产地",  "德国", R.drawable.arla));
        list.add(new Product("Devondale Milk", "¥ 79.00" ,   "产地",   "澳大利亚", R.drawable.devondale));
        list.add(new Product("Kindle Oasis", "¥ 2399.00",  "版本", "8GB", R.drawable.kindle));
        list.add(new Product("waitrose 早餐麦片", "¥ 179.00", "重量", "2Kg", R.drawable.waitrose));
        list.add(new Product("Mcvitie's 饼干", "¥ 14.90", "产地",   "英国", R.drawable.mcvitie));
        list.add(new Product("Ferrero Rocher", "¥ 132.59", "重量",  "300g", R.drawable.ferrero));
        list.add(new Product("Maltesers", "¥ 141.43", "重量",  "118g", R.drawable.maltesers));
        list.add(new Product("Lindt", "¥ 139.43", "重量",   "249g", R.drawable.lindt));
        list.add(new Product("Borggreve", "¥ 28.90",   "重量",  "640g", R.drawable.borggreve));
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        int productIndex = data.getIntExtra("productIndex", 0);
        boolean buy = data.getBooleanExtra("buy", false);
        if (buy) {
            if (requestCode == 0) {
                Map<String, Object> p1 = new HashMap<>();
                Product product = ((MyAdapter)rv.getAdapter()).getProduct(productIndex);
                p1.put("label", product.getLabel());
                p1.put("name", product.getName());
                p1.put("price", product.getPrice());
                p1.put("type", product.getType());
                p1.put("information", product.getInformation());
                p1.put("imgResource", product.getImgResource());
                productList.add(p1);
                productListAdapter.notifyDataSetChanged();
            } else if (requestCode == 1) {
                Map<String, Object> p1 = new HashMap<>();
                Map<String, Object> product = productList.get(productIndex);

                p1.put("label", product.get("label"));
                p1.put("name", product.get("name"));
                p1.put("price", product.get("price"));
                p1.put("type", product.get("type"));
                p1.put("information", product.get("information"));
                p1.put("imgResource", product.get("imgResource"));
                productList.add(p1);
                productListAdapter.notifyDataSetChanged();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
