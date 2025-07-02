package com.example.projectprmexe.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductDto> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductDto product);
        void onProductLongClick(ProductDto product);
    }

    public ProductAdapter(List<ProductDto> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDto product = productList.get(position);
        
        // Debug logging
        System.out.println("Binding product " + position + ": " + product.getName() + " - " + product.getPrice());
        
        holder.txtName.setText(product.getName() != null ? product.getName() : "No name");
        holder.txtDescription.setText(product.getDescription() != null ? product.getDescription() : "No description");
        holder.txtPrice.setText(product.getFormattedPrice());
        
        // Set availability status
        if (product.isAvailable()) {
            holder.txtAvailability.setText("Còn hàng");
            holder.txtAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.txtAvailability.setText("Hết hàng");
            holder.txtAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        // Load product image using Glide
        String imageUrl = product.getFirstImageUrl();
        System.out.println("Loading image for " + product.getName() + ": " + imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("sample_image_url")) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                            System.out.println("Failed to load image: " + imageUrl + " - Error: " + (e != null ? e.getMessage() : "Unknown"));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            System.out.println("Successfully loaded image: " + imageUrl);
                            return false;
                        }
                    })
                    .into(holder.imgProduct);
        } else {
            // Use placeholder for sample/empty URLs
            holder.imgProduct.setImageResource(R.drawable.image_placeholder);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onProductLongClick(product);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtPrice, txtAvailability;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtDescription = itemView.findViewById(R.id.txtProductDescription);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtAvailability = itemView.findViewById(R.id.txtProductAvailability);
        }
    }
}