package com.example.btl_task_management.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.R;
import com.example.btl_task_management.model.CongViec;
import java.util.List;

public class CongViecAdapter extends RecyclerView.Adapter<CongViecAdapter.ViewHolder> {
    private Context context;
    private List<CongViec> danhSachCongViec;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CongViec congViec);
        void onItemLongClick(CongViec congViec);
    }

    public CongViecAdapter(Context context, List<CongViec> danhSachCongViec, OnItemClickListener listener) {
        this.context = context;
        this.danhSachCongViec = danhSachCongViec;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cong_viec, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CongViec cv = danhSachCongViec.get(position);
        holder.tvTieuDe.setText(cv.getTieuDe());
        holder.tvMoTa.setText(cv.getMoTa());
        holder.tvNgayBatDau.setText("Bắt đầu: " + cv.getNgayBatDau());
        holder.tvNgayKetThuc.setText("Kết thúc: " + cv.getNgayKetThuc());
        holder.tvTrangThai.setText(cv.getTrangThai());
        holder.tvMucDoUuTien.setText(cv.getMucDoUuTien());
        
        holder.tvTrangThai.setTextColor(getMauTrangThai(cv.getTrangThai()));
        holder.tvMucDoUuTien.setTextColor(getMauUuTien(cv.getMucDoUuTien()));
        
        holder.cardView.setOnClickListener(v -> listener.onItemClick(cv));
        holder.cardView.setOnLongClickListener(v -> {
            listener.onItemLongClick(cv);
            return true;
        });
    }

    private int getMauTrangThai(String trangThai) {
        switch (trangThai) {
            case "Hoàn thành": return Color.parseColor("#4CAF50");
            case "Đang thực hiện": return Color.parseColor("#2196F3");
            default: return Color.parseColor("#FF9800");
        }
    }

    private int getMauUuTien(String uuTien) {
        switch (uuTien) {
            case "Cao": return Color.parseColor("#F44336");
            case "Trung bình": return Color.parseColor("#FF9800");
            default: return Color.parseColor("#4CAF50");
        }
    }

    @Override
    public int getItemCount() {
        return danhSachCongViec.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTieuDe, tvMoTa, tvNgayBatDau, tvNgayKetThuc, tvTrangThai, tvMucDoUuTien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            tvNgayBatDau = itemView.findViewById(R.id.tvNgayBatDau);
            tvNgayKetThuc = itemView.findViewById(R.id.tvNgayKetThuc);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvMucDoUuTien = itemView.findViewById(R.id.tvMucDoUuTien);
        }
    }
}
