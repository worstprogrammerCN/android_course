package com.example.administrator.final_project_kotlin

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

import java.util.ArrayList

/**
 * Created by worstProgrammerCN on 2017/1/1
 */

class MainActivityRecyclerViewAdapter(private var myData: ArrayList<Info>) : RecyclerView.Adapter<MainActivityRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    fun setData(a: ArrayList<Info>) {
        myData = a
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.linear_tab, viewGroup, false)
        val vh = ViewHolder(view)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return vh
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv.text = myData[position].title
        viewHolder.tv1.text = myData[position].context
        viewHolder.tv2.text = myData[position].time
        //viewHolder.iv.setBackground();
        viewHolder.iv.tag = "UNCLICKED"

        viewHolder.iv.setOnClickListener {
            val viewTag = viewHolder.iv.tag.toString()
            if (viewTag == "UNCLICKED") {
                viewHolder.iv.tag = "CLICKED"
                viewHolder.iv.setImageBitmap(downloadBitmap())
            } else {
                //Do nothing
            }
        }
        viewHolder.itemView.tag = position
    }

    override fun onClick(v: View) {
        mOnItemClickListener?.onItemClick(v, v.tag as Int)
    }

    override fun onLongClick(v: View): Boolean {
        mOnItemLongClickListener?.onItemLongClick(v, v.tag as Int)
        return false
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.mOnItemLongClickListener = listener
    }

    override fun getItemCount(): Int {
        return myData.size
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tv: TextView
        internal var tv1: TextView
        internal var tv2: TextView
        internal var iv: ImageButton

        init {
            tv = view.findViewById<View>(R.id.title) as TextView
            tv1 = view.findViewById<View>(R.id.context) as TextView
            tv2 = view.findViewById<View>(R.id.time) as TextView
            iv = view.findViewById<View>(R.id.previewImage) as ImageButton
        }
    }

    fun downloadBitmap(): Bitmap? {
        //Bitmap thisImage = new Bitmap();
        return null
    }

}
