package com.example.projectprmexe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprmexe.R;
import com.example.projectprmexe.data.model.PaymentDto;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<PaymentDto> paymentList;

    public PaymentAdapter(List<PaymentDto> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentDto payment = paymentList.get(position);
        holder.tvPaymentId.setText("#" + payment.getPaymentId());
        holder.tvOrderId.setText("Order: " + payment.getOrderId());
        holder.tvMethod.setText(payment.getPaymentMethod());
        holder.tvAmount.setText(String.format("%,.0f VND", payment.getPaidAmount()));
        holder.tvDate.setText(payment.getPaymentDate());
        holder.tvStatus.setText(payment.getStatus());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentId, tvOrderId, tvMethod, tvAmount, tvDate, tvStatus;
        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentId = itemView.findViewById(R.id.tvPaymentId);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvDate = itemView.findViewById(R.id.tvPaymentDate);
            tvStatus = itemView.findViewById(R.id.tvPaymentStatus);
        }
    }
} 