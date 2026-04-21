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
        CongViec congViec = danhSachCongViec.get(position);
        holder.tvTieuDe.setText(congViec.getTieuDe());
        holder.tvMoTa.setText(congViec.getMoTa());
        holder.tvNgayBatDau.setText("Bắt đầu: " + congViec.getNgayBatDau());
        holder.tvNgayKetThuc.setText("Kết thúc: " + congViec.getNgayKetThuc());
        holder.tvTrangThai.setText(congViec.getTrangThai());
        holder.tvMucDoUuTien.setText(congViec.getMucDoUuTien());
        if (congViec.getTrangThai().equals("Hoàn thành")) {
            holder.tvTrangThai.setTextColor(Color.parseColor("#4CAF50"));
        } else if (congViec.getTrangThai().equals("Đang thực hiện")) {
            holder.tvTrangThai.setTextColor(Color.parseColor("#2196F3"));
        } else {
            holder.tvTrangThai.setTextColor(Color.parseColor("#FF9800"));
        }
        if (congViec.getMucDoUuTien().equals("Cao")) {
            holder.tvMucDoUuTien.setTextColor(Color.parseColor("#F44336"));
        } else if (congViec.getMucDoUuTien().equals("Trung bình")) {
            holder.tvMucDoUuTien.setTextColor(Color.parseColor("#FF9800"));
        } else {
            holder.tvMucDoUuTien.setTextColor(Color.parseColor("#4CAF50"));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(congViec);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(congViec);
                return true;
            }
        });
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
