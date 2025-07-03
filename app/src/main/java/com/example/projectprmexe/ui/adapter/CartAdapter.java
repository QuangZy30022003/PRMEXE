package com.example.projectprmexe.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.ResponseBody;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.ProductAPI;
import com.example.projectprmexe.data.api.CartAPI;
import com.example.projectprmexe.data.model.Cart.CartItemResponseDTO;
import com.example.projectprmexe.data.model.Product.ProductDto;
import com.example.projectprmexe.data.repository.ProductInstance;
import com.example.projectprmexe.data.repository.CartInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import android.graphics.Paint;
import com.example.projectprmexe.data.model.Cart.CartItemCreateDTO;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItemResponseDTO> cartItems;
    private OnCartChangedListener onCartChangedListener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(List<CartItemResponseDTO> cartItems, OnCartChangedListener listener) {
        this.cartItems = cartItems;
        this.onCartChangedListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemResponseDTO item = cartItems.get(position);
        // Đảm bảo luôn lưu originalQuantity khi load
        if (item.getOriginalQuantity() == 0) {
            item.setOriginalQuantity(item.getQuantity());
        }
        holder.txtName.setText(item.getProductName());
        holder.edtQuantity.setText(String.valueOf(item.getQuantity()));

        // Gọi API lấy chi tiết sản phẩm để lấy ảnh và giá
        ProductAPI productAPI = ProductInstance.getApiService();
        productAPI.getProductById(item.getProductId()).enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(Call<ProductDto> call, Response<ProductDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDto product = response.body();
                    // Load ảnh
                    Glide.with(holder.itemView.getContext())
                            .load(product.getFirstImageUrl())
                            .placeholder(R.drawable.image_placeholder)
                            .into(holder.imgProduct);
                    // Hiển thị giá bán thực tế
                    holder.txtSalePrice.setText(String.format("%.0f VND", product.getPrice()));
                    // Hiển thị tổng tiền
                    double total = product.getPrice() * item.getQuantity();
                    holder.txtTotalPrice.setText("Tổng: " + String.format("%.0f VND", total));

                    // Xử lý cập nhật tổng tiền ngay khi thay đổi số lượng
                    holder.edtQuantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            int newQuantity = 1;
                            try {
                                newQuantity = Integer.parseInt(s.toString());
                            } catch (NumberFormatException e) {
                                newQuantity = 1;
                            }
                            if (newQuantity < 1) newQuantity = 1;
                            double newTotal = product.getPrice() * newQuantity;
                            holder.txtTotalPrice.setText("Tổng: " + String.format("%.0f VND", newTotal));
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });

                    // Gọi API cập nhật giỏ hàng khi mất focus
                    holder.edtQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            String text = holder.edtQuantity.getText().toString();
                            int newQuantity = 1;
                            try {
                                newQuantity = Integer.parseInt(text);
                            } catch (NumberFormatException e) {
                                newQuantity = 1;
                            }
                            if (newQuantity < 1) newQuantity = 1;
                            int originalQuantity = item.getOriginalQuantity();
                            int delta = newQuantity - originalQuantity;
                            if (delta != 0) {
                                // Gửi delta lên server, không gửi newQuantity
                                updateCartItemDelta(holder.itemView, item.getProductId(), delta, item, newQuantity);
                            }
                            if (onCartChangedListener != null) {
                                onCartChangedListener.onCartChanged();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ProductDto> call, Throwable t) {
                // Có thể log lỗi hoặc để trống
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            if (token == null) return;
            CartAPI cartAPI = CartInstance.getApiService();
            cartAPI.deleteCartItem(item.getProductId(), "Bearer " + token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                        Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối khi xóa!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSalePrice, txtTotalPrice;
        EditText edtQuantity;
        ImageView imgProduct;
        ImageButton btnDelete;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtSalePrice = itemView.findViewById(R.id.txtSalePrice);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            edtQuantity = itemView.findViewById(R.id.edtQuantity);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private double getOriginPrice(ProductDto product) {
        // Nếu ProductDto có trường originPrice thì trả về, nếu không thì trả về price
        // return product.getOriginPrice() != 0 ? product.getOriginPrice() : product.getPrice();
        return product.getPrice(); // Nếu không có giá gốc, chỉ trả về giá hiện tại
    }

    private void updateCartItem(View view, int productId, int quantity) {
        // Gọi API cập nhật số lượng sản phẩm trong giỏ hàng
        CartAPI cartAPI = CartInstance.getApiService();
        // Lấy token từ SharedPreferences
        android.content.Context context = view.getContext();
        android.content.SharedPreferences prefs = context.getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) return;
        CartItemCreateDTO dto = new CartItemCreateDTO(productId, quantity);
        cartAPI.addOrUpdateCart(dto, "Bearer " + token).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Cập nhật số lượng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối khi cập nhật số lượng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItemDelta(View view, int productId, int delta, CartItemResponseDTO item, int newQuantity) {
        CartAPI cartAPI = CartInstance.getApiService();
        android.content.Context context = view.getContext();
        android.content.SharedPreferences prefs = context.getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) return;
        CartItemCreateDTO dto = new CartItemCreateDTO(productId, delta);
        cartAPI.addOrUpdateCart(dto, "Bearer " + token).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Cập nhật lại originalQuantity nếu thành công
                    item.setOriginalQuantity(newQuantity);
                    item.setQuantity(newQuantity);
                } else {
                    Toast.makeText(context, "Cập nhật số lượng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối khi cập nhật số lượng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}