package com.example.administrator.shoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class product_detail extends AppCompatActivity {
    @BindView(R.id.product_image) ImageView product_image;
    int productIndex;
    @BindView(R.id.back) ImageView back;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.star) ImageView star;
    @BindView(R.id.image_panel) RelativeLayout image_panel;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.type) TextView type;
    @BindView(R.id.information) TextView information;
    @BindView(R.id.shop_button) ImageButton shopButton;
    @BindView(R.id.operationListView) ListView operationListView;
    int imgResource;
    boolean isInProductList = true;
    boolean wantBuy = false;

    private static final String DYNAMICATION = "myDynamicFilter";
    private static final String WIDGET_DYNAMIC_SATISFACTION = "MyDynamicWidgetReceiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ButterKnife.bind(this);

        int wid = getResources().getDisplayMetrics().widthPixels;
        int hei = getResources().getDisplayMetrics().heightPixels / 3;
        image_panel.setLayoutParams(new ConstraintLayout.LayoutParams(wid, hei));

        Intent intent = getIntent();

        String productName;
        String productPrice;
        String productType;
        String productInformation;
        this.productIndex = intent.getIntExtra("productIndex", 0);
        this.isInProductList = intent.getBooleanExtra("isInProductList", false);
        if (intent.hasExtra("product")) {
            Product product = (Product)intent.getParcelableExtra("product");
            productName = product.getName();
            productPrice = product.getPrice();
            productType = product.getType();
            productInformation = product.getInformation();
            this.imgResource = product.getImgResource();
        } else {
            productName =  intent.getStringExtra("name");
            productPrice = intent.getStringExtra("price");
            productType = intent.getStringExtra("type");
            productInformation = intent.getStringExtra("information");
            this.imgResource = intent.getIntExtra("imgResource", 0);
        }
        name.setText(productName);
        price.setText(productPrice);
        type.setText(productType);
        information.setText(productInformation);
        product_image.setImageResource(this.imgResource);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
        star.setTag(false);

        String[] opArr = {"一键下单", "分享产品", "不感兴趣", "查看更多商品促销信息"};
        operationListView.setAdapter(new ArrayAdapter<String>(this, R.layout.product_detail_operation, R.id.operationItem, opArr));
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Product product = (Product) intent.getParcelableExtra("product");
//        this.productIndex = intent.getIntExtra("productIndex", 0);
//        this.isInProductList = intent.getBooleanExtra("isInProductList", false);
//        this.imgResource = intent.getIntExtra("imgResource", 0);
//
//        name.setText(product.getName());
//        price.setText(product.getPrice());
//        type.setText(product.getType());
//        information.setText(product.getInformation());
//        product_image.setImageResource(this.imgResource);
//    }

    @OnClick(R.id.star)
    void changeStarState(View view) {
        boolean isFavor = (boolean) view.getTag();
        if (isFavor) {
            ((ImageView) view).setImageResource(R.drawable.full_star);
        } else {
            ((ImageView) view).setImageResource(R.drawable.empty_star);
        }
        view.setTag(!isFavor);
    }

    @OnClick(R.id.shop_button)
    void addResult() {
        if (this.wantBuy == false) {
            if (isInProductList)
                EventBus.getDefault().postSticky(new ProductListEvent(productIndex));
            else
                EventBus.getDefault().postSticky(new ShoppingListEvent(productIndex));

            // 广播，让MyReceiver接收以改变notification 通知栏
            IntentFilter dynamic_filter = new IntentFilter();
            dynamic_filter.addAction(DYNAMICATION);
            registerReceiver(DataBus.dynamicReceiver, dynamic_filter);

            Intent intent = new Intent(DYNAMICATION);
            intent.putExtra("name", name.getText());
            intent.putExtra("productIndex", productIndex);
            intent.putExtra("imgResource", imgResource);
            sendBroadcast(intent);

            // 广播，让AppWidget接收以改变widget
            IntentFilter widget_dynamic_filter = new IntentFilter();
            widget_dynamic_filter.addAction(WIDGET_DYNAMIC_SATISFACTION);
            registerReceiver(DataBus.widgetReceiver, widget_dynamic_filter);

            Intent intentWidget = new Intent(WIDGET_DYNAMIC_SATISFACTION);
            intentWidget.putExtra("name", name.getText());
            intentWidget.putExtra("imgResource", imgResource);
            sendBroadcast(intentWidget);

            // 显示"已添加到购物车"
            this.wantBuy = true;
            Toast.makeText(product_detail.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
        }
    }
}
