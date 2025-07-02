package com.example.projectprmexe.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ProductManagementViewHolder> {

    private List<ProductDto> productList;
    private OnProductManagementListener listener;

    public interface OnProductManagementListener {
        void onProductView(ProductDto product);
        void onProductEdit(ProductDto product);
        void onProductDelete(ProductDto product);
    }

    public ProductManagementAdapter(List<ProductDto> productList, OnProductManagementListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_management, parent, false);
        return new ProductManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManagementViewHolder holder, int position) {
        ProductDto product = productList.get(position);
        
        // Debug logging
        System.out.println("Binding management product " + position + ": " + product.getName());
        
        holder.txtName.setText(product.getName() != null ? product.getName() : "No name");
        holder.txtDescription.setText(product.getDescription() != null ? product.getDescription() : "No description");
        holder.txtPrice.setText(product.getFormattedPrice());
        holder.txtProductId.setText("ID: " + product.getProductId());
        
        // Set availability status
        if (product.isAvailable()) {
            holder.txtAvailability.setText("Available");
            holder.txtAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.txtAvailability.setText("Out of Stock");
            holder.txtAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        // Load product image using Glide
        String imageUrl = product.getFirstImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("sample_image_url")) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.image_placeholder);
        }

        // Set button click listeners
        holder.btnView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductView(product);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductEdit(product);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductDelete(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductManagementViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtPrice, txtAvailability, txtProductId;
        Button btnView, btnEdit, btnDelete;

        public ProductManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductManagement);
            txtName = itemView.findViewById(R.id.txtProductManagementName);
            txtDescription = itemView.findViewById(R.id.txtProductManagementDescription);
            txtPrice = itemView.findViewById(R.id.txtProductManagementPrice);
            txtAvailability = itemView.findViewById(R.id.txtProductManagementAvailability);
            txtProductId = itemView.findViewById(R.id.txtProductManagementId);
            btnView = itemView.findViewById(R.id.btnViewProduct);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}