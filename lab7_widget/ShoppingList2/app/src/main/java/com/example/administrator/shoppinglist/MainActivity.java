package com.example.administrator.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.shopListLayout) LinearLayout shopListLayout;

    @BindView(R.id.productListView) ListView productListView;
    SimpleAdapter productListAdapter;
    ArrayList<Map<String, Object>> productList = new ArrayList<>();

    @BindView(R.id.recyclerView) RecyclerView rv;

    @BindView(R.id.fab) FloatingActionButton fab;
    boolean isShopList = false;

    private final String BROADCAST_SATISFACTION = "onEnterMainActivity";
    private final String STATIC_SATISFACTION = "MyWidgetReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        setLayoutVisibility();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShopList = !isShopList;
                setLayoutVisibility();
            }
        });

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
                data.putBoolean("isInProductList", false);

                intent.putExtras(data);
                startActivityForResult(intent, 1);
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter(getData()));


        MyAdapter myAdapter = (MyAdapter) rv.getAdapter();
        int randomIndex = new Random().nextInt(myAdapter.getItemCount());
        Product product = myAdapter.getProduct(randomIndex);

        Intent intent = new Intent(BROADCAST_SATISFACTION);
        intent.putExtra("productIndex", randomIndex);
        intent.putExtra("product", (Parcelable) product);
        sendBroadcast(intent);

        Intent widgetIntent = new Intent(STATIC_SATISFACTION);
        widgetIntent.putExtra("productIndex", randomIndex);
        widgetIntent.putExtra("product", (Parcelable) product);
        sendBroadcast(widgetIntent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.isShopList = intent.getBooleanExtra("isShopList", false);
        setLayoutVisibility();
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Subscribe
    public void onProductListEvent(ProductListEvent event) {
        int productIndex = event.getProductIndex();
        Map<String, Object> p1 = new HashMap<>();
        Product product = ((MyAdapter)rv.getAdapter()).getProduct(productIndex);

        p1.put("label", product.getLabel());
        p1.put("name", product.getName());
        p1.put("price", product.getPrice());
        p1.put("type", product.getType());
        p1.put("information", product.getInformation());
        p1.put("imgResource", product.getImgResource());
        p1.put("productIndex", productIndex);
        productList.add(p1);
        productListAdapter.notifyDataSetChanged();
    };

    @Subscribe
    public void onShoppingListEvent(ShoppingListEvent event) {
        int productIndex = event.getProductIndex();
        Map<String, Object> p1 = new HashMap<>();
        Map<String, Object> product = productList.get(productIndex);

        p1.put("label", product.get("label"));
        p1.put("name", product.get("name"));
        p1.put("price", product.get("price"));
        p1.put("type", product.get("type"));
        p1.put("information", product.get("information"));
        p1.put("imgResource", product.get("imgResource"));
        p1.put("productIndex", productIndex);

        productList.add(p1);
        productListAdapter.notifyDataSetChanged();
    }

    public Product getProductListProduct(int pos) {
        return ((MyAdapter)rv.getAdapter()).getProduct(pos);
    }

    public void setLayoutVisibility() {
        if (isShopList) {
            rv.setVisibility(View.INVISIBLE);
            shopListLayout.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            shopListLayout.setVisibility(View.INVISIBLE);
        }
    }
}
