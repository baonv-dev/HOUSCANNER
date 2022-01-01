package com.example.btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl.R;

import java.util.ArrayList;

public class QuanLyHocKyAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> listHocKy;
    public QuanLyHocKyAdapter(Context c, ArrayList<String> listHocKyIn)
    {
        context =c;
        listHocKy = listHocKyIn;
    }
    @Override
    public int getCount() {
        return listHocKy.size();
    }

    @Override
    public Object getItem(int position) {
        return listHocKy.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.row_item_hoc_ky,null);
        }
        ImageView imgClass = convertView.findViewById(R.id.imageHocKy);
        TextView tenHocKy = convertView.findViewById(R.id.txtTenHocKy);
        imgClass.setImageResource(R.drawable.hocky);
        tenHocKy.setText(listHocKy.get(position).toString());

        return convertView;
    }
}
