package com.example.btl_task_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.R;
import com.example.btl_task_management.model.NguoiDung;
import java.util.List;

public class ThanhVienAdapter extends RecyclerView.Adapter<ThanhVienAdapter.ThanhVienViewHolder> {
    private Context context;
    private List<NguoiDung> danhSachThanhVien;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onXoaClick(NguoiDung nguoiDung);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(NguoiDung nguoiDung);
    }

    public ThanhVienAdapter(Context context, List<NguoiDung> danhSachThanhVien, OnItemClickListener listener) {
        this.context = context;
        this.danhSachThanhVien = danhSachThanhVien;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ThanhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thanh_vien, parent, false);
        return new ThanhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhVienViewHolder holder, int position) {
        NguoiDung nguoiDung = danhSachThanhVien.get(position);
        holder.tvTenThanhVien.setText(nguoiDung.getTenNguoiDung());
        holder.tvEmailThanhVien.setText(nguoiDung.getEmail());
        holder.btnXoaThanhVien.setOnClickListener(v -> {
            if (listener != null) listener.onXoaClick(nguoiDung);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(nguoiDung);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return danhSachThanhVien.size();
    }

    public static class ThanhVienViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenThanhVien, tvEmailThanhVien;
        Button btnXoaThanhVien;

        public ThanhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenThanhVien = itemView.findViewById(R.id.tvTenThanhVien);
            tvEmailThanhVien = itemView.findViewById(R.id.tvEmailThanhVien);
            btnXoaThanhVien = itemView.findViewById(R.id.btnXoaThanhVien);
        }
    }
}
