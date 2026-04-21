package com.example.btl_task_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.R;
import com.example.btl_task_management.model.DuAn;
import java.util.List;

public class DuAnAdapter extends RecyclerView.Adapter<DuAnAdapter.DuAnViewHolder> {
    private Context context;
    private List<DuAn> danhSachDuAn;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DuAn duAn);
    }

    public DuAnAdapter(Context context, List<DuAn> danhSachDuAn, OnItemClickListener listener) {
        this.context = context;
        this.danhSachDuAn = danhSachDuAn;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DuAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_du_an, parent, false);
        return new DuAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuAnViewHolder holder, int position) {
        DuAn duAn = danhSachDuAn.get(position);
        holder.tvTenDuAn.setText(duAn.getTenDuAn());
        holder.tvMoTaDuAn.setText(duAn.getMoTa());
        holder.tvNgayTao.setText("Ngày tạo: " + duAn.getNgayTao());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(duAn));
    }

    @Override
    public int getItemCount() {
        return danhSachDuAn.size();
    }

    public static class DuAnViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDuAn, tvMoTaDuAn, tvNgayTao;

        public DuAnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDuAn = itemView.findViewById(R.id.tvTenDuAn);
            tvMoTaDuAn = itemView.findViewById(R.id.tvMoTaDuAn);
            tvNgayTao = itemView.findViewById(R.id.tvNgayTao);
        }
    }
}
