package com.example.administrator.shoppinglist;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class product_detail extends AppCompatActivity {
    RelativeLayout image_panel;
    TextView name;
    TextView price;
    TextView type;
    TextView information;
    ImageView product_image;
    ListView operationListView;
    ImageView back;
    ImageView star;
    ImageButton shopButton;
    int productIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        image_panel = (RelativeLayout) findViewById(R.id.image_panel);
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        type = (TextView)findViewById(R.id.type);
        information = (TextView)findViewById(R.id.information);
        product_image = (ImageView)findViewById(R.id.product_image);
        operationListView = (ListView)findViewById(R.id.operationListView);
        back = (ImageView)findViewById(R.id.back);
        star = (ImageView)findViewById(R.id.star);
        shopButton = (ImageButton)findViewById(R.id.shop_button);

        int wid = getResources().getDisplayMetrics().widthPixels;
        int hei = getResources().getDisplayMetrics().heightPixels/3;
        image_panel.setLayoutParams(new ConstraintLayout.LayoutParams(wid,hei));

        final Bundle product = getIntent().getExtras();
        name.setText(product.getString("name"));
        price.setText(product.getString("price"));
        type.setText(product.getString("type"));
        information.setText(product.getString("information"));
        product_image.setImageResource(product.getInt("imgResource"));
        productIndex = product.getInt("productIndex");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        star.setTag(false);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavor = (boolean)view.getTag();
                if (isFavor) {
                    ((ImageView)view).setImageResource(R.drawable.full_star);
                } else {
                    ((ImageView)view).setImageResource(R.drawable.empty_star);
                }
                view.setTag(!isFavor);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addResult();
            }
        });

        String []opArr =  {"一键下单", "分享产品", "不感兴趣", "查看更多商品促销信息"};
        operationListView.setAdapter(new ArrayAdapter<String>(this, R.layout.product_detail_operation, R.id.operationItem, opArr));
    }

    void addResult() {
        Intent intent = new Intent(product_detail.this, MainActivity.class);
        intent.putExtra("productIndex", productIndex);
        intent.putExtra("buy", true);
        Toast.makeText(product_detail.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        setResult(0, intent);
    }
}
