package com.example.administrator.lab11_web.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lab11_web.R;
import com.example.administrator.lab11_web.model.Github;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.UserViewHolder> {
    ArrayList<Github> list;
    OnItemClickListener onItemClickListener;

    public CardAdapter(ArrayList<Github> list) {
        this.list = list;
    }

    public void updateData(ArrayList<Github> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_users, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        Github github =  list.get(position);
        holder.user_name.setText(github.getLogin());

        // 绑定item点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(position);
                    return true;
                }
            });
        }

        Integer id = github.getId();
        holder.user_id.setText(id.toString());
        holder.user_blog.setText(github.getBlog());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView user_name;
        TextView user_id;
        TextView user_blog;
        UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            user_id = (TextView)itemView.findViewById(R.id.user_id);
            user_blog = (TextView)itemView.findViewById(R.id.user_blog);
        }
    }

    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
}
