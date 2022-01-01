package com.example.btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl.dataClass.QuanLySinhVienLop;
import com.example.btl.R;

import java.util.ArrayList;

public class QuanLySinhVienLopAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<QuanLySinhVienLop> listSinhVien;

    public QuanLySinhVienLopAdapter(Context c,  ArrayList<QuanLySinhVienLop> listSinhVienIn)
    {
        context =c;
        this.listSinhVien = listSinhVienIn;
    }

    @Override
    public int getCount() {
        return listSinhVien.size();
    }

    @Override
    public Object getItem(int position) {
        return listSinhVien.get(position);
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
            convertView = inflater.inflate(R.layout.row_items_sinh_vien,null);
        }
        ImageView imgClass = convertView.findViewById(R.id.icon_sinhvien);
        TextView tenSinhVien = convertView.findViewById(R.id.txtTenSinhVien);
        TextView maSinhVien = convertView.findViewById(R.id.txtMaSV);
        imgClass.setImageResource(R.drawable.student);
        tenSinhVien.setText(listSinhVien.get(position).getHoTen()+"-"+listSinhVien.get(position).getLop());
        maSinhVien.setText(listSinhVien.get(position).getMaSV()+'-'+listSinhVien.get(position).getNgaySinh());
        return convertView;
    }
}
