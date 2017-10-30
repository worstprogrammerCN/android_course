package com.example.administrator.shoppinglist;

import com.example.administrator.shoppinglist.Product;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.TextView;
import android.widget.Toast;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
    ArrayList<Product> products;

    public MyAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Product product = products.get(position);
        System.out.print(product.getName());
        holder.label.setText(product.getName().substring(0, 1));
        holder.name.setText((product.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), product_detail.class);

                Bundle data = new Bundle();
                data.putInt("productIndex", position);
                data.putString("name", product.getName());
                data.putString("price", product.getPrice());
                data.putString("type", product.getType());
                data.putString("information", product.getInformation());
                data.putInt("imgResource", product.getImgResource());
                data.putBoolean("isInProductList", true);
                intent.putExtras(data);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ((Activity)view.getContext()).startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                products.remove(position);
                MyAdapter.this.notifyDataSetChanged();
                Toast.makeText(view.getContext(), "移除第" + position + "个商品", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public Product getProduct(int pos) {
        return products.get(pos);
    }

//    public void addProduct(Product product) {
//        products.add(product);
//        this.notifyDataSetChanged();
//    }

    class ItemViewHolder extends ViewHolder {

        public TextView label;
        public TextView name;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.label = itemView.findViewById(R.id.label);
            this.name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
