package com.example.btl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.btl.dataClass.AttendanceManually;
import com.example.btl.R;

import java.util.List;

public class AttendanceManuallyAdapter extends BaseAdapter {

    private List<AttendanceManually> lstAttMan;
    private Activity context;

    public AttendanceManuallyAdapter(List<AttendanceManually> lstAttMan, Activity context) {
        this.lstAttMan = lstAttMan;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstAttMan.size();
    }

    @Override
    public Object getItem(int position) {
        return lstAttMan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView tvTenSV,tvMaSV,tvLopNgaySinh;
        CheckBox cbDiemDanh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
       if(convertView==null){
           convertView = inflater.inflate(R.layout.row_attendance_manually, null);
           holder= new ViewHolder();
           holder.tvLopNgaySinh = convertView.findViewById(R.id.lop_ngaysinh);
           holder.tvMaSV = convertView.findViewById(R.id.masv);
           holder.tvTenSV = convertView.findViewById(R.id.tensv);
           holder.cbDiemDanh = convertView.findViewById(R.id.checkboxDiemDanh);
           convertView.setTag(holder);
       }else {
           holder = (ViewHolder) convertView.getTag();
       }
       AttendanceManually attendanceManually = lstAttMan.get(position);
       holder.tvTenSV.setText(attendanceManually.getHoTen());
       holder.tvMaSV.setText(attendanceManually.getMaSV());
       holder.tvLopNgaySinh.setText(attendanceManually.getLop()+" - "+attendanceManually.getNgaySinh());
       if(attendanceManually.getDadiemdanh()) {
           holder.cbDiemDanh.setChecked(true);
       }
       else
       {
           holder.cbDiemDanh.setChecked(false);
       }
       return convertView;
    }
}
