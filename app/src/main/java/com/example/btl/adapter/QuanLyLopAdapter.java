package com.example.btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl.dataClass.QuanLyLop;
import com.example.btl.R;

import java.util.List;

//import static com.example.btl.activity.AttendanceBarcodeActivity.dataListLop;

public class QuanLyLopAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<QuanLyLop> lstLop;
    public QuanLyLopAdapter(Context c,List<QuanLyLop> listClass)
    {
        context =c;
        this.lstLop = listClass;
    }

    @Override
    public int getCount() {
        return lstLop.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.row_item_class,null);
        }
        ImageView imgClass = convertView.findViewById(R.id.icon_classroom);
        TextView tenlop = convertView.findViewById(R.id.txtTenLop);
        TextView sotinChi = convertView.findViewById(R.id.txtSoTinChi);
        TextView soBuoi = convertView.findViewById(R.id.txtSoBuoi);
        TextView maLop = convertView.findViewById(R.id.txtMaLop);

        imgClass.setImageResource(R.drawable.classroom);
        if(lstLop.size()!=0) {
            tenlop.setText(lstLop.get(position).getTenLop());
            sotinChi.setText(lstLop.get(position).getSoTinChi().toString());
            soBuoi.setText(lstLop.get(position).getSoBuoi().toString());
            maLop.setText(lstLop.get(position).getMaLop());
        }
        else
        {
            Toast.makeText(context, "Hiện chưa có lớp", Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }
}
