package com.example.btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl.R;
import com.example.btl.dataClass.ThongKe;

import java.util.ArrayList;

public class ThongKeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    //private String[] listStudent;
    private ArrayList<ThongKe> lstThongKe;

    public ThongKeAdapter(Context c,  ArrayList<ThongKe> lstThongKe)
    {
        context =c;
        this.lstThongKe = lstThongKe;
    }

    @Override
    public int getCount() {
        return lstThongKe.size();
    }

    @Override
    public Object getItem(int position) {
        return lstThongKe.get(position);
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
            convertView = inflater.inflate(R.layout.row_thongke_lop,null);
        }
        ImageView imgClass = convertView.findViewById(R.id.icon_sinhvien_thongke);
        TextView tenSinhVien = convertView.findViewById(R.id.txtTenSinhVienThongKe);
        TextView soBuoiDi = convertView.findViewById(R.id.sobuoidihoc);
        TextView maSV = convertView.findViewById(R.id.txtMaSVThongKe);
        imgClass.setImageResource(R.drawable.student);
        if(lstThongKe.size() == 0)
        {
            //tenSinhVien.setText("Không có sinh viên nào thuộc lớp này");
        }
        else {
            tenSinhVien.setText(lstThongKe.get(position).getHoTen() + "-" + lstThongKe.get(position).getLop());
            soBuoiDi.setText("Số buổi đi học : " + String.valueOf(lstThongKe.get(position).getDiemDanh().size()));
            maSV.setText(lstThongKe.get(position).getMaSV() + "-" + lstThongKe.get(position).getNgaySinh());
        }

        return convertView;
    }
}
