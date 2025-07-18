package com.example.projectprmexe.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.example.projectprmexe.data.api.CartAPI;
import com.example.projectprmexe.data.model.Cart.CartItemCreateDTO;
import com.example.projectprmexe.data.repository.CartInstance;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.example.projectprmexe.BuildConfig;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductDto> productList;
    private OnProductClickListener listener;
    private String role = "user";

    public interface OnProductClickListener {
        void onProductClick(ProductDto product);
        void onProductLongClick(ProductDto product);
    }

    public ProductAdapter(List<ProductDto> productList, OnProductClickListener listener, String role) {
        this.productList = productList;
        this.listener = listener;
        this.role = role;
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

        // Load product image using Glide (ban đầu)
        String imageUrl = product.getFirstImageUrl();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imgProduct);

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

        // Ẩn nút thêm vào giỏ hàng nếu là admin
        if ("4".equals(role)) {
            holder.btnAddToCart.setVisibility(View.GONE);
        } else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setOnClickListener(v -> {
                Context context = holder.itemView.getContext();
                SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String token = prefs.getString("token", null);
                if (token == null) {
                    Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }
                CartAPI cartAPI = CartInstance.getApiService();
                CartItemCreateDTO dto = new CartItemCreateDTO(product.getProductId(), 1);
                cartAPI.addOrUpdateCart(dto, "Bearer " + token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtPrice, txtAvailability;
        ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtDescription = itemView.findViewById(R.id.txtProductDescription);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtAvailability = itemView.findViewById(R.id.txtProductAvailability);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}