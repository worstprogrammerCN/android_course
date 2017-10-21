package com.example.jj.shoppinglist;

import com.example.jj.shoppinglist.Product;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.TextView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
    ArrayList<Product> products;

    public MyAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.label.setText(products.get(position).getName().charAt(0));
        holder.name.setText((products.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    class ItemViewHolder extends ViewHolder {

        public TextView label;
        public TextView name;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.label = (TextView)itemView.findViewById(R.id.label);
            this.name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
