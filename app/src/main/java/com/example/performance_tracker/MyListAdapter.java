package com.example.performance_tracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MyListAdapter extends BaseAdapter {
    private List<Task>  mData;
    private Context mContext;

    public MyListAdapter(final Context context, final List<Task> mData){
        this.mData = mData;
        this.mContext = context;
    }

    public List<Task> getData(){
        return this.mData;
    }

    @Override
    public int getCount() {
        return mData!=null? mData.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mData!=null? mData.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
